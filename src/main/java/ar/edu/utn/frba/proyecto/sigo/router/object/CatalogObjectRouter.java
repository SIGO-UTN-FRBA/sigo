package ar.edu.utn.frba.proyecto.sigo.router.object;

import ar.edu.utn.frba.proyecto.sigo.dto.common.EnumerationDTO;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.object.CatalogObjectService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class CatalogObjectRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private CatalogObjectService catalogService;

    @Inject
    public CatalogObjectRouter(
        JsonTransformer jsonTransformer,
        CatalogObjectService catalogService
    ){
        super(null, null);

        this.jsonTransformer = jsonTransformer;
        this.catalogService = catalogService;
    }

    private final Route fetchLightings = (Request request, Response response) -> {
        return Arrays.stream(catalogService.listLightingTypes())
                .map(v -> EnumerationDTO.builder()
                        .id(v.ordinal())
                        .name(v.name())
                        .description(v.type())
                        .build()
                ).collect(Collectors.toList());
    };

    private final Route fetchMarkIndicators = (Request request, Response response) -> {
        return Arrays.stream(catalogService.markIndicatorsTypes())
                .map(v -> EnumerationDTO.builder()
                            .id(v.ordinal())
                            .name(v.name())
                            .description(v.type())
                            .build()
                )
                .collect(Collectors.toList());
    };

    private final Route fetchObjectTypes = (Request request, Response response) -> {
        return Arrays.stream(catalogService.fetchObjectTypes())
                .map(v -> EnumerationDTO.builder()
                            .id(v.ordinal())
                            .name(v.name())
                            .description(v.type())
                            .build()
                )
                .collect(Collectors.toList());
    };


    @Override
    public RouteGroup routes() {
        return () -> {
            get("/objectTypes", fetchObjectTypes, jsonTransformer);
            get("/lightings", fetchLightings, jsonTransformer);
            get("/markIndicators", fetchMarkIndicators, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/catalogs/objects";
    }
}
