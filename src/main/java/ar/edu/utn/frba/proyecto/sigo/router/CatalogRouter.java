package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirectionPosition;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.CatalogService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static spark.Spark.get;

public class CatalogRouter extends SigoRouter {

    private final JsonTransformer jsonTransformer;
    private CatalogService catalogService;

    @Inject
    public CatalogRouter(
            HibernateUtil hibernateUtil,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            CatalogService catalogService
    ) {
        this.jsonTransformer = jsonTransformer;
        this.catalogService = catalogService;
        this.objectMapper = objectMapper;
        this.hibernateUtil = hibernateUtil;
    }

    private final Route fetchRunwaySurface = doInTransaction( false,(Request request, Response response) -> catalogService.findAllRunwaySurfaces());

    private final Route fetchRunwayDirectionPosition = doInTransaction(false, (Request request, Response response) -> RunwayDirectionPosition.values());

    private final Route fetchAirportRegulations = doInTransaction(false, (Request request, Response response) -> catalogService.findAllAirportRegulations());

    private final Route fetchRunwayTypeLetterICAO = doInTransaction(false, (Request request, Response response) -> catalogService.findAllRunwayTypeLetterICAO());

    private final Route fetchRunwayTypeNumberICAO = doInTransaction(false, (Request request, Response response) -> catalogService.findAllRunwayTypeNumberICAO());

    @Override
    public RouteGroup routes() {
        return () -> {
            get("/runways/surfaces", fetchRunwaySurface, jsonTransformer);
            get("/runways/directions/positions", fetchRunwayDirectionPosition, jsonTransformer);
            get("/runways/types/icao/letters", fetchRunwayTypeLetterICAO, jsonTransformer);
            get("/runways/types/icao/numbers", fetchRunwayTypeNumberICAO, jsonTransformer);
            get("/airports/regulations", fetchAirportRegulations, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/catalog";
    }
}
