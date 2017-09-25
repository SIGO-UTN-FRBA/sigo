package ar.edu.utn.frba.proyecto.sigo.rest.airport;

import ar.edu.utn.frba.proyecto.sigo.commons.rest.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.dto.AirportDTO;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Point;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.lang.String.*;
import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

@Singleton
public class AirportRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AirportService airportService;
    private AirportTranslator translator;

    @Inject
    public AirportRouter(
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            AirportService airportService,
            AirportTranslator translator
    ) {
        this.jsonTransformer = jsonTransformer;
        this.airportService = airportService;
        this.objectMapper = objectMapper;
        this.translator = translator;
    }

    /**
     * Get a list of airports instances filtered by values of its properties
     */
    private final Route fetchAirports = (Request request, Response response) -> {

        return airportService.find(request.queryMap())
                .stream()
                .map(translator::getAsDTO)
                .collect(toList());
    };

    /**
     * Update airport's properties
     */
    private final Route updateAirport = (Request request, Response response) -> {

        AirportDTO dto = translator.getAsDTO(request.body());

        Airport airport = translator.getAsDomain(dto);

        return translator.getAsDTO(airportService.update(airport));
    };

    /**
     * Create an airport
     */
    private final Route createAirport = (Request request, Response response) -> {

        AirportDTO dto = translator.getAsDTO(request.body());

        Airport airport = translator.getAsDomain(dto);

        airportService.create(airport);

        return translator.getAsDTO(airport);
    };

    /**
     * Delete an airport given its identifier
     */
    private final Route deleteAirport = (Request request, Response response) -> {

        airportService.delete(getParamAirportId(request));

        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    };

    /**
     * Get an airport given its identifier
     */
    private final Route fetchAirport = (Request request, Response response) -> {

        Airport airport = airportService.get(getParamAirportId(request));

        return translator.getAsDTO(airport);
    };

    /**
     * Get point given an airport identifier
     */
    private final Route fetchGeometry = (Request request, Response response) -> {

        Airport airport = airportService.get(getParamAirportId(request));

        return airport.getGeom();
    };

    /**
     * Create point for an airport
     */
    private final Route defineGeometry = (Request request, Response response) -> {

        Point point = objectMapper.fromJson(request.body(), Point.class);

        Airport airport = airportService.get(getParamAirportId(request));

        airportService.defineGeometry(point, airport);

        return point;
    };

    @SuppressWarnings("Duplicates")
    @Override
    public RouteGroup routes() {
        return ()-> {
            get("", fetchAirports, jsonTransformer);
            post("", createAirport,jsonTransformer);

            get(format("/:%s", AIRPORT_ID_PARAM), fetchAirport, jsonTransformer);
            put(format("/:%s", AIRPORT_ID_PARAM), updateAirport, jsonTransformer);
            delete(format("/:%s", AIRPORT_ID_PARAM), deleteAirport);

            get(format("/:%s/geometry", AIRPORT_ID_PARAM), fetchGeometry, jsonTransformer);
            post(format("/:%s/geometry", AIRPORT_ID_PARAM), defineGeometry, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/airports";
    }
}
