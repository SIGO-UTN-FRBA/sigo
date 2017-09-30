package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.RunwaySurfaceService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static spark.Spark.get;

public class CatalogRouter extends SigoRouter {

    private final JsonTransformer jsonTransformer;
    private RunwaySurfaceService runwaySurfaceService;

    @Inject
    public CatalogRouter(
            HibernateUtil hibernateUtil,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            RunwaySurfaceService runwaySurfaceService
    ) {
        this.jsonTransformer = jsonTransformer;
        this.runwaySurfaceService = runwaySurfaceService;
        this.objectMapper = objectMapper;
        this.hibernateUtil = hibernateUtil;
    }

    private final Route fetchRunwaySurface = doInTransaction( false,(Request request, Response response) -> {
        return runwaySurfaceService.findAll();
    });

    @Override
    public RouteGroup routes() {
        return () -> {
            get("/runwaySurfaces", fetchRunwaySurface, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/catalog";
    }
}
