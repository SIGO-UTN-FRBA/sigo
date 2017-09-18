package ar.edu.utn.frba.proyecto.sigo.airport;

import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.common.collect.Lists;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;

@Singleton
public class AirportRouter implements Router{

    private JsonTransformer jsonTransformer;

    @Inject
    public AirportRouter(
        JsonTransformer jsonTransformer
    ){
        this.jsonTransformer = jsonTransformer;
    }

    private final Route fetchAirports = (Request request, Response response) -> {
        return Lists.newArrayList();
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
