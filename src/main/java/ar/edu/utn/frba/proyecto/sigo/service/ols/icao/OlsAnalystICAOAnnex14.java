package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.icao.RunwayClassificationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionRule;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surfaces;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalyst;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Service;
import com.google.common.collect.Lists;
import com.google.inject.assistedinject.Assisted;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OlsAnalystICAOAnnex14 extends OlsAnalyst {

    private OlsRuleICAOAnnex14Service definitionService;
    private ICAOAnnex14SurfaceGeometriesHelper geometryHelper;

    @Inject
    public OlsAnalystICAOAnnex14(
            OlsRuleICAOAnnex14Service service,
            ICAOAnnex14SurfaceGeometriesHelper geometryHelper,
            HibernateUtil hibernateUtil,
            @Assisted AnalysisCase analysisCase
    ) {
        super(analysisCase, hibernateUtil.getSessionFactory());

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
                .map(object -> AnalysisObstacle.builder()
                                    .object(object)
                                    .surface(surface)
                                    .analysisCase(this.getAnalysisCase())
                                    .objectHeight(object.getPlacedObject().getHeightAmls())
                                    //TODO .surfaceHeight()
                                    .surfaceHeight(0D)
                                    .excluded(false)
                                    .build()
                )
                .collect(Collectors.toSet());
    }

    private boolean isObstacle(AnalysisSurface surface, AnalysisObject object) {
        return surface.getSurface().getGeometry().covers(object.getPlacedObject().getGeom());
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
        AnalysisSurface analysisSurfaceForInnerHorizontal = createInnerHorizontalAnalysisSurface(direction,innerHorizontalDefinition, (ICAOAnnex14SurfaceStrip) stripAnalysisSurface.getSurface());
        analysisSurfaces.add(analysisSurfaceForInnerHorizontal);

        //3. transicion

        //4. conica
        ICAOAnnex14SurfaceConical conicalDefinition = (ICAOAnnex14SurfaceConical) surfacesDefinitions.stream().filter(d -> d.getEnum() == ICAOAnnex14Surfaces.CONICAL).findFirst().get();
        AnalysisSurface conicalAnalysisSurface = createConicalAnalysisSurface(direction, conicalDefinition, (ICAOAnnex14SurfaceInnerHorizontal) analysisSurfaceForInnerHorizontal.getSurface(), (ICAOAnnex14SurfaceStrip) stripAnalysisSurface.getSurface());
        analysisSurfaces.add(conicalAnalysisSurface);

        //5. aprox
        //6. despegue
        //7. horizontal externa

        return analysisSurfaces;
    }

    private AnalysisSurface createStripAnalysisSurface(RunwayDirection direction, ICAOAnnex14SurfaceStrip stripDefinition) {

        ICAOAnnex14Surface surface = applyRuleException(stripDefinition);

        surface.setGeometry(geometryHelper.createStripSurfaceGeometry(direction, stripDefinition));

        return AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(surface)
                .direction(direction)
                .build();
    }

    private AnalysisSurface createInnerHorizontalAnalysisSurface(RunwayDirection direction, ICAOAnnex14SurfaceInnerHorizontal innerHorizontalDefinition, ICAOAnnex14SurfaceStrip stripSurface) {

        ICAOAnnex14Surface surface = applyRuleException(innerHorizontalDefinition);

        surface.setGeometry(geometryHelper.createInnerHorizontalSurfaceGeometry(direction, innerHorizontalDefinition, stripSurface));

        return AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(surface)
                .direction(direction)
                .build();
    }

    private AnalysisSurface createConicalAnalysisSurface(
            RunwayDirection direction,
            ICAOAnnex14SurfaceConical conicalDefinition,
            ICAOAnnex14SurfaceInnerHorizontal innerHorizontalSurface,
            ICAOAnnex14SurfaceStrip stripSurface
    ){

        ICAOAnnex14Surface surface = applyRuleException(conicalDefinition);

        surface.setGeometry(geometryHelper.createConicalSurfaceGeometry(direction, conicalDefinition, innerHorizontalSurface, stripSurface));

        return AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(surface)
                .direction(direction)
                .build();
    }

    private ICAOAnnex14Surface applyRuleException(ICAOAnnex14Surface surface) {

        this.analysisCase.getRuleExceptions()
                .filter( s -> ((OlsRuleICAOAnnex14)s.getRule()).getSurface().equals(surface.getEnum()))
                .findFirst()
                .ifPresent(s -> applyRuleException(surface, s));

        return surface;
    }

    private void applyRuleException(ICAOAnnex14Surface surface, AnalysisExceptionRule exception) {

        String setter = String.format("set%s", StringUtils.capitalize(((OlsRuleICAOAnnex14)exception.getRule()).getPropertyCode()));

        try {
            surface.getClass().getMethod(setter,Double.class).invoke(surface,exception.getValue());

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
