package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.dto.RunwayDirectionDTO;
import ar.edu.utn.frba.proyecto.sigo.service.RunwayDirectionTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.service.RunwayService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class RunwayDirectionRouter extends SigoRouter{

    private JsonTransformer jsonTransformer;
    private RunwayService runwayService;
    private RunwayDirectionService directionService;
    private RunwayDirectionTranslator translator;

    @Inject
    public RunwayDirectionRouter(
        Gson objectMapper,
        HibernateUtil hibernateUtil,
        JsonTransformer jsonTransformer,
        RunwayService runwayService,
        RunwayDirectionService directionService,
        RunwayDirectionTranslator translator
    ){
        this.jsonTransformer = jsonTransformer;
        this.runwayService = runwayService;
        this.directionService = directionService;
        this.translator = translator;
        this.objectMapper = objectMapper;
        this.hibernateUtil = hibernateUtil;
    }

    private final Route fetchDirections = doInTransaction(false, (Request request, Response response)-> {

        Runway runway = runwayService.get(getParamRunwayId(request));

        return runwayService.getDirections(runway)
                .stream()
                .map(translator::getAsDTO)
                .collect(toList());
    });

    private final Route createDirection = doInTransaction(true, (Request request, Response response)-> {

        RunwayDirectionDTO dto = translator.getAsDTO(request.body());

        Runway runway = runwayService.get(getParamRunwayId(request));

        RunwayDirection direction = translator.getAsDomain(dto);

        directionService.create(direction, runway);

        return translator.getAsDTO(direction);
    });

    private final Route updateDirection = doInTransaction(true, (request, response) -> {

        RunwayDirectionDTO dto = objectMapper.fromJson(request.body(), RunwayDirectionDTO.class);

        RunwayDirection direction = translator.getAsDomain(dto);

        return translator.getAsDTO(directionService.update(direction));
    });


    /**
     * Create point for a runway
     */
    private final Route defineGeometry = doInTransaction(true, (Request request, Response response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        Point geometry = objectMapper.fromJson(request.body(), Point.class);

        directionService.defineGeometry(geometry, direction);

        return geometry;
    });


    /**
     * Get point for a runway direction
     */
    private final Route fetchGeometry = doInTransaction(false, (Request request, Response response) -> {

        RunwayDirection direction = directionService.get(getParamDirectionId(request));

        return direction.getGeom();
    });

    @Override
    public RouteGroup routes() {
        return () -> {
            get("", fetchDirections, jsonTransformer);
            post("", createDirection, jsonTransformer);

            put("/:" + RUNWAY_DIRECTION_ID_PARAM, updateDirection, jsonTransformer);

            get(format("/:%s/geometry", RUNWAY_DIRECTION_ID_PARAM), fetchGeometry, jsonTransformer);
            post(format("/:%s/geometry", RUNWAY_DIRECTION_ID_PARAM), defineGeometry, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/airports/:"+ AIRPORT_ID_PARAM +"/runways/:" + RUNWAY_ID_PARAM + "/directions";
    }
}