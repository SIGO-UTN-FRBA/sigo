package ar.edu.utn.frba.proyecto.sigo.router.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirectionPositions;
import ar.edu.utn.frba.proyecto.sigo.dto.RunwayDirectionPositionDTO;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.airport.CatalogAirportService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class CatalogAirportRouter extends SigoRouter {

    private final JsonTransformer jsonTransformer;
    private CatalogAirportService catalogService;

    @Inject
    public CatalogAirportRouter(
            HibernateUtil hibernateUtil,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            CatalogAirportService catalogService
    ) {
        this.jsonTransformer = jsonTransformer;
        this.catalogService = catalogService;
        this.objectMapper = objectMapper;
        this.hibernateUtil = hibernateUtil;
    }

    private final Route fetchRunwaySurface = doInTransaction( false,(Request request, Response response) -> catalogService.findAllRunwaySurfaces());

    private final Route fetchRunwayDirectionPosition = (Request request, Response response) -> {
        return Arrays.stream(RunwayDirectionPositions.values())
                        .map(p -> RunwayDirectionPositionDTO.builder()
                                        .id(p.ordinal())
                                        .code(p.position())
                                        .description(p.name())
                                        .build()
                        ).collect(Collectors.toList());
    };

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
            get("/regulations", fetchAirportRegulations, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/catalogs/airports";
    }
}
