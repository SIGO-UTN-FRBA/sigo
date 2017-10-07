package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.Runway;
import ar.edu.utn.frba.proyecto.sigo.dto.RunwayDTO;
import ar.edu.utn.frba.proyecto.sigo.service.RunwayTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.AirportService;
import ar.edu.utn.frba.proyecto.sigo.service.RunwayService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Polygon;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

public class RunwayRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private RunwayService runwayService;
    private AirportService airportService;
    private RunwayTranslator translator;

    @Inject
    public RunwayRouter(
        Gson objectMapper,
        HibernateUtil hibernateUtil,
        JsonTransformer jsonTransformer,
        RunwayService runwayService,
        AirportService airportService,
        RunwayTranslator runwayTranslator
    ){

        this.jsonTransformer = jsonTransformer;
        this.runwayService = runwayService;
        this.airportService = airportService;
        this.translator = runwayTranslator;
        this.objectMapper = objectMapper;
        this.hibernateUtil = hibernateUtil;
    }

    /**
     * Get runways releated to an airport
     */
    private final Route fetchRunways = doInTransaction(false, (Request request, Response response) ->{

        Airport airport = airportService.get(getParamAirportId(request));

        return airportService.getRunways(airport)
                .stream()
                .map(translator::getAsDTO)
                .collect(toList());
    });

    /**
     * Get a runway given its identifier
     */
    private final Route fetchRunway = doInTransaction(false, (Request request, Response response) -> {

        Runway runway = runwayService.get(getParamRunwayId(request));

        return translator.getAsDTO(runway);
    });

    /**
     * Create a runway for a given airport
     */
    private final Route createRunway = doInTransaction(true, (Request request, Response response) -> {

        RunwayDTO runwayDTO = translator.getAsDTO(request.body());

        Airport airport = airportService.get(getParamAirportId(request));

        Runway runway = translator.getAsDomain(runwayDTO);

        runwayService.create(runway, airport);

        return translator.getAsDTO(runway);
    });

    /**
     * Create polygon for a runway
     */
    private final Route defineGeometry = doInTransaction(true, (Request request, Response response) -> {

        Runway runway = runwayService.get(getParamRunwayId(request));

        Polygon geometry = objectMapper.fromJson(request.body(), Polygon.class);

        runwayService.defineGeometry(geometry, runway);

        return geometry;
    });

    /**
     * Get polygon for a runway
     */
    private final Route fetchGeometry = doInTransaction(false, (Request request, Response response) -> {

        Runway runway = runwayService.get(getParamRunwayId(request));

        return runway.getGeom();
    });

    /**
     * Update runway's properties
     */
    private final Route updateRunway = doInTransaction(true, (Request request, Response response) -> {

        RunwayDTO runwayDTO = objectMapper.fromJson(request.body(), RunwayDTO.class);

        Runway runway = translator.getAsDomain(runwayDTO);

        return translator.getAsDTO(runwayService.update(runway));
    });

    /**
     * Delete a runway by id
     */
    private final Route deleteRunway = doInTransaction(true, (Request request, Response response) -> {

        runwayService.delete(getParamRunwayId(request));

        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    });

    @SuppressWarnings("Duplicates")
    @Override
    public RouteGroup routes() {
        return () -> {
            get("", fetchRunways, jsonTransformer);
            post("", createRunway, jsonTransformer);

            get(format("/:%s", RUNWAY_ID_PARAM), fetchRunway, jsonTransformer);
            put(format("/:%s", RUNWAY_ID_PARAM), updateRunway, jsonTransformer);
            delete(format("/:%s", RUNWAY_ID_PARAM), deleteRunway);

            get(format("/:%s/geometry", RUNWAY_ID_PARAM), fetchGeometry, jsonTransformer);
            post(format("/:%s/geometry", RUNWAY_ID_PARAM), defineGeometry, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/airports/:"+ AIRPORT_ID_PARAM +"/runways";
    }
}
