package ar.edu.utn.frba.proyecto.sigo.airport;

import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;

@Singleton
@AllArgsConstructor(onConstructor = @__(@Inject))
public class AirportRouter implements Router{

    private JsonTransformer jsonTransformer;
    private AirportService airportService;

    private final Route fetchAirports = (Request request, Response response) -> {
        return airportService.findAll();
    };

    @Override
    public RouteGroup routes() {
        return ()->{
            get("", fetchAirports, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/airports";
    }
}
