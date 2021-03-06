package ar.edu.utn.frba.proyecto.sigo.router.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.*;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.*;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.airport.*;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.translator.SimpleFeatureTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.RunwayApproachSectionTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.RunwayClassificationTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.RunwayDirectionTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.RunwayTakeoffSectionTranslator;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Point;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.SessionFactory;
import org.opengis.feature.simple.SimpleFeature;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

public class RunwayDirectionRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private RunwayService runwayService;
    private RunwayDirectionService directionService;
    private RunwayDirectionTranslator directionTranslator;
    private RunwayApproachSectionTranslator approachTranslator;
    private RunwayTakeoffSectionTranslator takeoffTranslator;
    private RunwayApproachSectionService approachService;
    private RunwayTakeoffSectionService takeoffService;
    private RunwayClassificationService classificationService;
    private RunwayClassificationTranslator classificationTranslator;
    private RunwayStripService stripService;
    private SimpleFeatureTranslator featureTranslator;

    @Inject
    public RunwayDirectionRouter(
        Gson objectMapper,
        SessionFactory sessionFactory,
        JsonTransformer jsonTransformer,
        RunwayService runwayService,
        RunwayDirectionService directionService,
        RunwayDirectionTranslator directionTranslator,
        RunwayApproachSectionTranslator approachTranslator,
        RunwayTakeoffSectionTranslator takeoffTranslator,
        RunwayApproachSectionService approachService,
        RunwayTakeoffSectionService takeoffService,
        RunwayClassificationService classificationService,
        RunwayClassificationTranslator classificationTranslator,
        RunwayStripService stripService,
        SimpleFeatureTranslator featureTranslator
    ){
        super(objectMapper, sessionFactory);

        this.jsonTransformer = jsonTransformer;
        this.runwayService = runwayService;
        this.directionService = directionService;
        this.directionTranslator = directionTranslator;
        this.approachTranslator = approachTranslator;
        this.takeoffTranslator = takeoffTranslator;
        this.approachService = approachService;
        this.takeoffService = takeoffService;
        this.classificationService = classificationService;
        this.classificationTranslator = classificationTranslator;
        this.stripService = stripService;
        this.featureTranslator = featureTranslator;
    }

    /**
     * Get directions for a runway
     */
    private final Route fetchDirections = doInTransaction(false, (request, response)-> {

        Runway runway = runwayService.get(getParamRunwayId(request));

        return runwayService.getDirections(runway)
                .stream()
                .map(directionTranslator::getAsDTO)
                .collect(toList());
    });

    /**
     * Create a direction
     */
    private final Route createDirection = doInTransaction(true, (request, response)-> {

        RunwayDirectionDTO dto = directionTranslator.getAsDTO(request.body());

        Runway runway = runwayService.get(getParamRunwayId(request));

        RunwayDirection direction = directionTranslator.getAsDomain(dto);

        directionService.create(direction, runway);

        return directionTranslator.getAsDTO(direction);
    });

    /**
     * Get instance of a direction
     */
    private final Route fetchDirection = doInTransaction(false, (request, response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        return directionTranslator.getAsDTO(direction);
    });

    /**
     * Update a direction given an id
     */
    private final Route updateDirection = doInTransaction(true, (request, response) -> {

        RunwayDirectionDTO dto = objectMapper.fromJson(request.body(), RunwayDirectionDTO.class);

        RunwayDirection direction = directionTranslator.getAsDomain(dto);

        return directionTranslator.getAsDTO(directionService.update(direction));
    });

    /**
     * Delete a direction given an id
     */
    private final Route deleteDirection = doInTransaction(true, (request, response)->{

        directionService.delete(getParamDirectionId(request));

        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    });


    /**
     * Update runway direction's geometry (point)
     */
    private final Route updateFeature = doInTransaction(true, (request, response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        SimpleFeature feature = featureTranslator.getAsDomain(objectMapper.fromJson(request.body(), JsonObject.class));

        directionService.updateGeometry((Point)feature.getDefaultGeometry(), direction);

        return direction.getGeom();
    });


    /**
     * Get runway direction as feature
     */
    private final Route fetchFeature = doInTransaction(false, (request, response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        return featureTranslator.getAsDTO(directionService.getFeature(direction));
    });

    /**
     * Get approach section for a runway direction
     */
    private final Route fetchApproachSection = doInTransaction(false, (request, response) -> {
        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        return this.approachTranslator.getAsDTO(direction.getApproachSection());
    });

    /**
     * Update approach section for a runway direction
     */
    private final Route updateApproachSection = doInTransaction(true,(request, response) -> {
        RunwayApproachSectionDTO dto = objectMapper.fromJson(request.body(), RunwayApproachSectionDTO.class);

        RunwayApproachSection domain = approachTranslator.getAsDomain(dto);

        this.approachService.update(domain);

        return approachTranslator.getAsDTO(domain);
    });

    /**
     * Get takeoff section for a runway direction
     */
    private final Route fetchTakeoffSection = doInTransaction(false, (request, response) -> {
        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        return this.takeoffTranslator.getAsDTO(direction.getTakeoffSection());
    });

    /**
     * Update takeoff section for a runway direction
     */
    private final Route updateTakeoffSection = doInTransaction(true, (request, response) -> {
        RunwayTakeoffSectionDTO dto = objectMapper.fromJson(request.body(), RunwayTakeoffSectionDTO.class);

        RunwayTakeoffSection domain = takeoffTranslator.getAsDomain(dto);

        this.takeoffService.update(domain);

        return takeoffTranslator.getAsDTO(domain);
    });

    /**
     * Calculate declared distance of a runway direction
     */
    private final Route calculateDistances = doInTransaction(false, (request, response)-> {
        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        List<RunwayDistanceDTO> distances = Lists.newArrayList();

        distances.add(
            RunwayDistanceDTO.builder()
                        .shortName(RunwayDistances.TODA.shortName())
                        .largeName(RunwayDistances.TODA.largeName())
                        .description(RunwayDistances.TODA.description())
                        .length(RunwayDistanceHelper.calculateTODALength(direction))
                        .build()
        );

        distances.add(
                RunwayDistanceDTO.builder()
                        .shortName(RunwayDistances.TORA.shortName())
                        .largeName(RunwayDistances.TORA.largeName())
                        .description(RunwayDistances.TORA.description())
                        .length(RunwayDistanceHelper.calculateTORALength(direction))
                        .build()
        );

        distances.add(
                RunwayDistanceDTO.builder()
                        .shortName(RunwayDistances.ASDA.shortName())
                        .largeName(RunwayDistances.ASDA.largeName())
                        .description(RunwayDistances.ASDA.description())
                        .length(RunwayDistanceHelper.calculateASDALength(direction))
                        .build()
        );

        distances.add(
                RunwayDistanceDTO.builder()
                        .shortName(RunwayDistances.LDA.shortName())
                        .largeName(RunwayDistances.LDA.largeName())
                        .description(RunwayDistances.LDA.description())
                        .length(RunwayDistanceHelper.calculateLDALength(direction))
                        .build()
        );

        return distances;
    });

    private final Route getThresholdFeature = doInTransaction(false, (request, response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        return featureTranslator.getAsDTO(approachService.getThresholdFeature(direction));
    });

    private final Route getClearwayFeature = doInTransaction(false, (request, response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        return featureTranslator.getAsDTO(takeoffService.getClearwayFeature(direction));
    });

    private final Route getStopwayFeature = doInTransaction(false, (request, response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        return featureTranslator.getAsDTO(takeoffService.getStopwayFeature(direction));
    });

    private final Route fetchClassification = doInTransaction(false, (request, response) -> {
        RunwayClassification classification = directionService.get(getParamDirectionId(request)).getClassification();

        return classificationTranslator.getAsDTO(classification);
    });

    private final Route updateClassification = doInTransaction(true, (request, response) -> {

        RunwayClassificationDTO dto = this.objectMapper.fromJson(request.body(), RunwayClassificationDTO.class);

        RunwayClassification domain = this.classificationTranslator.getAsDomain(dto);

        this.classificationService.update(domain);

        return classificationTranslator.getAsDTO(domain);
    });

    private final Route fetchStrip = doInTransaction(false, (request, response) -> {
        RunwayDirection direction = this.directionService.get(getParamDirectionId(request));

        return direction.getStrip();
    });

    private final Route updateStrip = doInTransaction(true, (request, response) -> {

        RunwayStrip strip = objectMapper.fromJson(request.body(), RunwayStrip.class);

        stripService.update(strip);

        return strip;
    });

    @Override
    public RouteGroup routes() {
        return () -> {
            get("", fetchDirections, jsonTransformer);
            post("", createDirection, jsonTransformer);

            get("/:" + RUNWAY_DIRECTION_ID_PARAM, fetchDirection, jsonTransformer);
            put("/:" + RUNWAY_DIRECTION_ID_PARAM, updateDirection, jsonTransformer);
            delete("/:" + RUNWAY_DIRECTION_ID_PARAM, deleteDirection);

            get(format("/:%s/classification", RUNWAY_DIRECTION_ID_PARAM), fetchClassification, jsonTransformer);
            put(format("/:%s/classification", RUNWAY_DIRECTION_ID_PARAM), updateClassification, jsonTransformer);

            get(format("/:%s/feature", RUNWAY_DIRECTION_ID_PARAM), fetchFeature, jsonTransformer);
            patch(format("/:%s/feature", RUNWAY_DIRECTION_ID_PARAM), updateFeature, jsonTransformer);

            get(format("/:%s/strip", RUNWAY_DIRECTION_ID_PARAM), fetchStrip, jsonTransformer);
            put(format("/:%s/strip", RUNWAY_DIRECTION_ID_PARAM), updateStrip, jsonTransformer);

            get(format("/:%s/distances", RUNWAY_DIRECTION_ID_PARAM), calculateDistances, jsonTransformer);

            get(format("/:%s/sections/approach", RUNWAY_DIRECTION_ID_PARAM), fetchApproachSection, jsonTransformer);
            put(format("/:%s/sections/approach", RUNWAY_DIRECTION_ID_PARAM), updateApproachSection, jsonTransformer);
            get(format("/:%s/sections/approach/threshold/feature", RUNWAY_DIRECTION_ID_PARAM), getThresholdFeature, jsonTransformer);

            get(format("/:%s/sections/takeoff", RUNWAY_DIRECTION_ID_PARAM), fetchTakeoffSection, jsonTransformer);
            put(format("/:%s/sections/takeoff", RUNWAY_DIRECTION_ID_PARAM), updateTakeoffSection, jsonTransformer);
            get(format("/:%s/sections/takeoff/clearway/feature", RUNWAY_DIRECTION_ID_PARAM), getClearwayFeature, jsonTransformer);
            get(format("/:%s/sections/takeoff/stopway/feature", RUNWAY_DIRECTION_ID_PARAM), getStopwayFeature, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/airports/:"+ AIRPORT_ID_PARAM +"/runways/:" + RUNWAY_ID_PARAM + "/directions";
    }
}
