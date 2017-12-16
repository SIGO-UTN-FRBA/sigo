package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.icao.RunwayClassificationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surfaces;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisExceptionService;
import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalyst;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Service;
import com.google.common.collect.Lists;
import com.google.inject.assistedinject.Assisted;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OlsAnalystICAOAnnex14 extends OlsAnalyst {

    private OlsRuleICAOAnnex14Service definitionService;
    private ICAOAnnex14SurfaceGeometriesHelper geometryHelper;
    private SessionFactory sessionFactory;

    @Inject
    public OlsAnalystICAOAnnex14(
            OlsRuleICAOAnnex14Service service,
            ICAOAnnex14SurfaceGeometriesHelper geometryHelper,
            HibernateUtil hibernateUtil,
            @Assisted AnalysisCase analysisCase
    ) {
        super(analysisCase);

        this.sessionFactory = hibernateUtil.getSessionFactory();
        this.definitionService = service;
        this.geometryHelper = geometryHelper;
    }

    @Override
    protected void applyExceptions() {

    }

    @Override
    protected void defineObstacles() {

        Set<AnalysisObstacle> obstacles = this.getAnalysisCase().getSurfaces()
                .stream()
                .map(this::defineObstacles)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        this.analysisCase.setObstacles(obstacles);
    }

    private Set<AnalysisObstacle> defineObstacles(AnalysisSurface surface) {
        return this.getAnalysisCase().getObjects()
                .stream()
                .filter(object -> isObstacle(surface, object))
                .map( object -> AnalysisObstacle.builder()
                                    .object(object)
                                    .surface(surface)
                                    .build()
                )
                .collect(Collectors.toSet());
    }

    private boolean isObstacle(AnalysisSurface surface, AnalysisObject object) {

        //TODO intersectar en altura (evaluando propiedades)

        return surface.getGeometry().covers(object.getPlacedObject().getGeom());
    }

    @Override
    protected void initializeSurfaces() {

        this.analysisCase.setSurfaces(createAnalysisSurfaces());
    }


    private List<AnalysisSurface> createAnalysisSurfaces() {

        return this.analysisCase.getAerodrome().getRunways()
                .stream()
                .map(Runway::getDirections)
                .flatMap(Collection::stream)
                .map(this::createAnalysisSurfaces)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<AnalysisSurface> createAnalysisSurfaces(RunwayDirection direction){

        RunwayClassificationICAOAnnex14 classification = (RunwayClassificationICAOAnnex14) direction.getClassification();

        List<ICAOAnnex14Surface> surfacesDefinitions = this.definitionService.getSurfaces(classification.getRunwayClassification(), classification.getRunwayCategory(), classification.getRunwayTypeNumber(), false);

        switch (classification.getRunwayClassification()) {
            case NON_INSTRUMENT:
                return createAnalysisSurfacesForNonInstrument(direction, surfacesDefinitions);
            case NON_PRECISION_APPROACH:
                return createAnalysisSurfacesForNonInstrument(direction, surfacesDefinitions); //TODO createAnalysisSurfacesForNonPrecision
            case PRECISION_APPROACH:
                return createAnalysisSurfacesForNonInstrument(direction, surfacesDefinitions); //TODO createAnalysisSurfacesForPrecision
        }

        throw new SigoException("Invalid classification of runway direction");
    }

    private List<AnalysisSurface> createAnalysisSurfacesForNonInstrument(RunwayDirection direction, List<ICAOAnnex14Surface> surfacesDefinitions) {

        List<AnalysisSurface> analysisSurfaces = Lists.newArrayList();

        //1. franja
        ICAOAnnex14SurfaceStrip stripDefinition = (ICAOAnnex14SurfaceStrip) surfacesDefinitions.stream().filter(d -> d.getEnum() == ICAOAnnex14Surfaces.STRIP).findFirst().get();
        AnalysisSurface stripAnalysisSurface = createStripAnalysisSurface(direction, stripDefinition);
        analysisSurfaces.add(stripAnalysisSurface);

        //2. horizontal interna
        ICAOAnnex14SurfaceInnerHorizontal innerHorizontalDefinition = (ICAOAnnex14SurfaceInnerHorizontal) surfacesDefinitions.stream().filter(d -> d.getEnum() == ICAOAnnex14Surfaces.INNER_HORIZONTAL).findFirst().get();
        AnalysisSurface analysisSurfaceForInnerHorizontal = createInnerHorizontalAnalysisSurface(direction,innerHorizontalDefinition);
        analysisSurfaces.add(analysisSurfaceForInnerHorizontal);

        //3. transicion
        //4. conica
        //5. aprox
        //6. despegue
        //7. horizontal externa

        return analysisSurfaces;
    }

    private AnalysisSurface createInnerHorizontalAnalysisSurface(RunwayDirection direction, ICAOAnnex14SurfaceInnerHorizontal innerHorizontalDefinition) {
        AnalysisSurface analysisSurface = AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(applyRuleException(innerHorizontalDefinition))
                .geometry(geometryHelper.createInnerHorizontalSurfaceGeometry(direction, innerHorizontalDefinition))
                .direction(direction)
                .build();

        getCurrentSession().persist(analysisSurface);

        return analysisSurface;
    }

    private AnalysisSurface createStripAnalysisSurface(RunwayDirection direction, ICAOAnnex14SurfaceStrip stripDefinition) {
        AnalysisSurface analysisSurface = AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(applyRuleException(stripDefinition))
                .geometry(geometryHelper.createStripSurfaceGeometry(direction, stripDefinition))
                .direction(direction)
                .build();

        getCurrentSession().persist(analysisSurface);

        return analysisSurface;
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    private ICAOAnnex14Surface applyRuleException(ICAOAnnex14Surface surface) {

        //TODO buscar excepciones relacionadas al caso y actualizar valores de propiedades

        return surface;
    }
}
