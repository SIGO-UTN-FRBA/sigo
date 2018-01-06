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
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproach;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachFirstSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachSecondSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceTransitional;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surfaces;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalyst;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Service;
import com.google.common.collect.Lists;
import com.google.inject.assistedinject.Assisted;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
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
    private ICAOAnnex14SurfaceHeightsHelper heightsHelper;

    @Inject
    public OlsAnalystICAOAnnex14(
            OlsRuleICAOAnnex14Service service,
            ICAOAnnex14SurfaceGeometriesHelper geometryHelper,
            HibernateUtil hibernateUtil,
            ICAOAnnex14SurfaceHeightsHelper heightsHelper,
            @Assisted AnalysisCase analysisCase
    ) {
        super(analysisCase, hibernateUtil.getSessionFactory());

        this.definitionService = service;
        this.geometryHelper = geometryHelper;
        this.heightsHelper = heightsHelper;
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
                .map(object -> createAnalysisObstacle(surface, object))
                .collect(Collectors.toSet());
    }

    private boolean isObstacle(AnalysisSurface surface, AnalysisObject object) {
        return surface.getSurface().getGeometry().covers(object.getPlacedObject().getGeom());
    }

    private AnalysisObstacle createAnalysisObstacle(AnalysisSurface surface, AnalysisObject object) {

        Double surfaceHeight = determineSurfaceHeight(surface, object);

        Double objectHeight = object.getPlacedObject().getHeightAmls();

        return AnalysisObstacle.builder()
                            .object(object)
                            .surface(surface)
                            .analysisCase(this.getAnalysisCase())
                            .objectHeight(objectHeight)
                            .surfaceHeight(surfaceHeight)
                            .excluded((surfaceHeight - objectHeight) > 0)
                            .build();
    }

    private Double determineSurfaceHeight(AnalysisSurface surface, AnalysisObject object) {

        Coordinate intersection = surface.getSurface().getGeometry()
                .intersection(object.getPlacedObject().getGeom())
                .getInteriorPoint()
                .getCoordinate();

        Double surfaceHeight = this.heightsHelper.heightAtCoordinate((ICAOAnnex14Surface) surface.getSurface(), intersection);

        return surface.getDirection().getHeight() + surfaceHeight;
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

        //3. conica
        ICAOAnnex14SurfaceConical conicalDefinition = (ICAOAnnex14SurfaceConical) surfacesDefinitions.stream().filter(d -> d.getEnum() == ICAOAnnex14Surfaces.CONICAL).findFirst().get();
        AnalysisSurface conicalAnalysisSurface = createConicalAnalysisSurface(direction, conicalDefinition, (ICAOAnnex14SurfaceInnerHorizontal) analysisSurfaceForInnerHorizontal.getSurface(), (ICAOAnnex14SurfaceStrip) stripAnalysisSurface.getSurface());
        analysisSurfaces.add(conicalAnalysisSurface);

        //4. aprox 1
        ICAOAnnex14SurfaceApproach approachDefinition = (ICAOAnnex14SurfaceApproach) surfacesDefinitions.stream().filter(d -> d.getEnum() == ICAOAnnex14Surfaces.APPROACH).findFirst().get();
        ICAOAnnex14SurfaceApproachFirstSection approachFirstSectionDefinition = (ICAOAnnex14SurfaceApproachFirstSection) surfacesDefinitions.stream().filter(d -> d.getEnum() == ICAOAnnex14Surfaces.APPROACH_FIRST_SECTION).findFirst().get();
        AnalysisSurface approachFirstSectionAnalysisSurface = createApproachFirstSectionAnalysisSurface(direction, approachDefinition, approachFirstSectionDefinition, (ICAOAnnex14SurfaceStrip) stripAnalysisSurface.getSurface());
        analysisSurfaces.add(approachFirstSectionAnalysisSurface);

        //4. aprox 2
        ICAOAnnex14SurfaceApproachSecondSection approachSecondSectionDefinition = (ICAOAnnex14SurfaceApproachSecondSection) surfacesDefinitions.stream().filter(d -> d.getEnum() == ICAOAnnex14Surfaces.APPROACH_SECOND_SECTION).findFirst().get();
        AnalysisSurface approachSecondSectionAnalysisSurface = createApproachSecondSectionAnalysisSurface(direction, approachSecondSectionDefinition, approachDefinition, (ICAOAnnex14SurfaceApproachFirstSection) approachFirstSectionAnalysisSurface.getSurface());
        analysisSurfaces.add(approachSecondSectionAnalysisSurface);

        //5. transicion
        ICAOAnnex14SurfaceTransitional transitionalDefinition = (ICAOAnnex14SurfaceTransitional) surfacesDefinitions.stream().filter(d -> d.getEnum() == ICAOAnnex14Surfaces.TRANSITIONAL).findFirst().get();
        AnalysisSurface transitionalAnalysisSurface =  createTransitionalAnalysisSurface(direction, transitionalDefinition, stripDefinition, approachFirstSectionDefinition, innerHorizontalDefinition);
        analysisSurfaces.add(transitionalAnalysisSurface);

        //6. despegue
        //7. horizontal externa

        return analysisSurfaces;
    }

    private AnalysisSurface createStripAnalysisSurface(RunwayDirection direction, ICAOAnnex14SurfaceStrip stripSurface) {

        applyRuleException(stripSurface);

        stripSurface.setGeometry(geometryHelper.createStripSurfaceGeometry(direction, stripSurface));

        return AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(stripSurface)
                .direction(direction)
                .build();
    }

    private AnalysisSurface createInnerHorizontalAnalysisSurface(
            RunwayDirection direction,
            ICAOAnnex14SurfaceInnerHorizontal innerHorizontalSurface,
            ICAOAnnex14SurfaceStrip stripSurface
    ) {

        applyRuleException(innerHorizontalSurface);

        innerHorizontalSurface.setGeometry(geometryHelper.createInnerHorizontalSurfaceGeometry(direction, innerHorizontalSurface, stripSurface));

        return AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(innerHorizontalSurface)
                .direction(direction)
                .build();
    }

    private AnalysisSurface createConicalAnalysisSurface(
            RunwayDirection direction,
            ICAOAnnex14SurfaceConical conicalSurface,
            ICAOAnnex14SurfaceInnerHorizontal innerHorizontalSurface,
            ICAOAnnex14SurfaceStrip stripSurface
    ){

        //1. acondiciono superficie
        applyRuleException(conicalSurface);

        conicalSurface.setGeometry(geometryHelper.createConicalSurfaceGeometry(direction, conicalSurface, innerHorizontalSurface, stripSurface));

        conicalSurface.setInitialHeight(innerHorizontalSurface.getHeight());

        //2. creo el analisis de la superfie
        return AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(conicalSurface)
                .direction(direction)
                .build();
    }

    private AnalysisSurface createApproachFirstSectionAnalysisSurface(
            RunwayDirection direction,
            ICAOAnnex14SurfaceApproach approach,
            ICAOAnnex14SurfaceApproachFirstSection approachFirstSection,
            ICAOAnnex14SurfaceStrip strip
    ) {

        approachFirstSection.setInitialHeight(direction.getApproachSection().getThresholdElevation());

        approachFirstSection.setGeometry(geometryHelper.createApproachFirstSectionSurfaceGeometry(direction,approach, approachFirstSection, strip));

        return AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(approachFirstSection)
                .direction(direction)
                .build();
    }

    private AnalysisSurface createApproachSecondSectionAnalysisSurface(
            RunwayDirection direction,
            ICAOAnnex14SurfaceApproachSecondSection approachSecondSection,
            ICAOAnnex14SurfaceApproach approach,
            ICAOAnnex14SurfaceApproachFirstSection approachFirstSection) {

        double adjacent = approachFirstSection.getLength();
        double degrees = Math.atan(approachFirstSection.getSlope() / 100);
        double hypotenuse = adjacent / Math.cos(degrees);
        double opposite = Math.sqrt(Math.pow(hypotenuse,2) - Math.pow(adjacent,2));
        approachSecondSection.setInitialHeight(approachFirstSection.getInitialHeight() + opposite);

        approachSecondSection.setGeometry(geometryHelper.createApproachSecondSectionSurfaceGeometry(direction, approachSecondSection, approach, approachFirstSection));

        return AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(approachSecondSection)
                .direction(direction)
                .build();
    }

    private AnalysisSurface createTransitionalAnalysisSurface(
            RunwayDirection direction,
            ICAOAnnex14SurfaceTransitional transitional,
            ICAOAnnex14SurfaceStrip strip,
            ICAOAnnex14SurfaceApproachFirstSection approach,
            ICAOAnnex14SurfaceInnerHorizontal innerHorizontal)
    {
        transitional.setInitialHeight(direction.getHeight());

        double opposite = innerHorizontal.getHeight();
        double angle = Math.atan(transitional.getSlope()/100);
        double hypotenuse = opposite / Math.sin(angle);
        double adjacent = Math.sqrt(Math.pow(hypotenuse,2)-Math.pow(opposite,2));
        transitional.setWidth(adjacent);

        transitional.setGeometry(geometryHelper.createTransitionalSurfaceGeometry(direction, transitional, strip, approach, innerHorizontal));

        return AnalysisSurface.builder()
                .analysisCase(this.analysisCase)
                .surface(transitional)
                .direction(direction)
                .build();
    }

    private <T extends ICAOAnnex14Surface> void applyRuleException(T surface) {

        this.analysisCase.getRuleExceptions()
                .filter( s -> ((OlsRuleICAOAnnex14)s.getRule()).getSurface().equals(surface.getEnum()))
                .findFirst()
                .ifPresent(s -> applyRuleException(surface, s));
    }

    private <T extends ICAOAnnex14Surface> void applyRuleException(T surface, AnalysisExceptionRule exception) {

        String setter = String.format("set%s", StringUtils.capitalize(((OlsRuleICAOAnnex14)exception.getRule()).getPropertyCode()));

        try {
            surface.getClass().getMethod(setter,Double.class).invoke(surface,exception.getValue());

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
