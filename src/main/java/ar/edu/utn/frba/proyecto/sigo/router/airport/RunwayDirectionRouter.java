package ar.edu.utn.frba.proyecto.sigo.router.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.*;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayApproachSectionDTO;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayDistanceDTO;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayTakeoffSectionDTO;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayDirectionDTO;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.airport.*;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Point;
import org.eclipse.jetty.http.HttpStatus;
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

    @Inject
    public RunwayDirectionRouter(
        Gson objectMapper,
        HibernateUtil hibernateUtil,
        JsonTransformer jsonTransformer,
        RunwayService runwayService,
        RunwayDirectionService directionService,
        RunwayDirectionTranslator directionTranslator,
        RunwayApproachSectionTranslator approachTranslator,
        RunwayTakeoffSectionTranslator takeoffTranslator,
        RunwayApproachSectionService approachService,
        RunwayTakeoffSectionService takeoffService
    ){
        this.jsonTransformer = jsonTransformer;
        this.runwayService = runwayService;
        this.directionService = directionService;
        this.directionTranslator = directionTranslator;
        this.approachTranslator = approachTranslator;
        this.takeoffTranslator = takeoffTranslator;
        this.approachService = approachService;
        this.takeoffService = takeoffService;
        this.objectMapper = objectMapper;
        this.hibernateUtil = hibernateUtil;
    }

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
     * Create point for a runway direction
     */
    private final Route defineGeometry = doInTransaction(true, (request, response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        Point geometry = objectMapper.fromJson(request.body(), Point.class);

        directionService.defineGeometry(geometry, direction);

        return geometry;
    });


    /**
     * Get point for a runway direction
     */
    private final Route fetchGeometry = doInTransaction(false, (request, response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        return direction.getGeom();
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

    @Override
    public RouteGroup routes() {
        return () -> {
            get("", fetchDirections, jsonTransformer);
            post("", createDirection, jsonTransformer);

            get("/:" + RUNWAY_DIRECTION_ID_PARAM, fetchDirection, jsonTransformer);
            put("/:" + RUNWAY_DIRECTION_ID_PARAM, updateDirection, jsonTransformer);
            delete("/:" + RUNWAY_DIRECTION_ID_PARAM, deleteDirection);

            get(format("/:%s/geometry", RUNWAY_DIRECTION_ID_PARAM), fetchGeometry, jsonTransformer);
            post(format("/:%s/geometry", RUNWAY_DIRECTION_ID_PARAM), defineGeometry, jsonTransformer);

            get(format("/:%s/distances", RUNWAY_DIRECTION_ID_PARAM), calculateDistances, jsonTransformer);

            get(format("/:%s/sections/approach", RUNWAY_DIRECTION_ID_PARAM), fetchApproachSection, jsonTransformer);
            put(format("/:%s/sections/approach", RUNWAY_DIRECTION_ID_PARAM), updateApproachSection, jsonTransformer);

            get(format("/:%s/sections/takeoff", RUNWAY_DIRECTION_ID_PARAM), fetchTakeoffSection, jsonTransformer);
            put(format("/:%s/sections/takeoff", RUNWAY_DIRECTION_ID_PARAM), updateTakeoffSection, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/airports/:"+ AIRPORT_ID_PARAM +"/runways/:" + RUNWAY_ID_PARAM + "/directions";
    }
}
