package ar.edu.utn.frba.proyecto.sigo.airport;

import ar.edu.utn.frba.proyecto.sigo.commons.rest.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.*;

@Singleton
public class AirportRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AirportService airportService;

    @Inject
    public AirportRouter(
            JsonTransformer jsonTransformer,
            AirportService airportService) {
        this.jsonTransformer = jsonTransformer;
        this.airportService = airportService;
    }

    /**
     * Get a list of airports instances filtered by values of its properties
     */
    private final Route fetchAirports = (Request request, Response response) -> {

        return airportService.find(request.queryMap());
    };

    /**
     * Update airport's properties
     */
    private final Route updateAirport = (Request request, Response response) -> {

        Airport airport = gson.fromJson(request.body(), Airport.class);

        airportService.update(airport);

        return airport;
    };

    /**
     * Create an airport
     */
    private final Route createAirport = (Request request, Response response) -> {

        Airport airport = gson.fromJson(request.body(), Airport.class);

        return airportService.create(airport);
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

        return airportService.get(getParamAirportId(request));
    };

    @Override
    public RouteGroup routes() {
        return ()->{
            get("", fetchAirports, jsonTransformer);
            post("", createAirport,jsonTransformer);

            get("/:airport_id", fetchAirport, jsonTransformer);
            put("/:airport_id", updateAirport, jsonTransformer);
            delete("/:airport_id", deleteAirport);
        };
    }

    @Override
    public String path() {
        return "/airports";
    }
}
