package ar.edu.utn.frba.proyecto.sigo.router.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirectionPositions;
import ar.edu.utn.frba.proyecto.sigo.dto.common.EnumerationDTO;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.airport.CatalogAirportService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import org.hibernate.SessionFactory;
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
            SessionFactory sessionFactory,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            CatalogAirportService catalogService
    ) {
        super(objectMapper, sessionFactory);

        this.jsonTransformer = jsonTransformer;
        this.catalogService = catalogService;
    }

    private final Route fetchRunwaySurface = (Request request, Response response) -> {
        return Arrays.stream(this.catalogService.findAllRunwaySurfaces())
                        .map(s -> new EnumerationDTO(s.ordinal(), s.name(), s.description()))
                        .collect(Collectors.toList());
    };

    private final Route fetchRunwayDirectionPosition = (Request request, Response response) -> {
        return Arrays.stream(RunwayDirectionPositions.values())
                .map(p -> new EnumerationDTO(p.ordinal(), p.name(), p.position()))
                .collect(Collectors.toList());
    };

    private final Route fetchAirportRegulations = doInTransaction(false, (Request request, Response response) ->
            catalogService.findAllAirportRegulations().collect(Collectors.toList())
    );

    @Override
    public RouteGroup routes() {
        return () -> {
            get("/runways/surfaces", fetchRunwaySurface, jsonTransformer);
            get("/runways/directions/positions", fetchRunwayDirectionPosition, jsonTransformer);
            get("/regulations", fetchAirportRegulations, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/catalogs/airports";
    }
}
