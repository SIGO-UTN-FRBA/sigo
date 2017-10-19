package ar.edu.utn.frba.proyecto.sigo.router.object;

import ar.edu.utn.frba.proyecto.sigo.dto.object.LightingTypeDTO;
import ar.edu.utn.frba.proyecto.sigo.dto.object.MarkIndicatorTypeDTO;
import ar.edu.utn.frba.proyecto.sigo.dto.object.PlacedObjectTypeDTO;
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

        this.jsonTransformer = jsonTransformer;
        this.catalogService = catalogService;
    }

    private final Route fetchLightings = (Request request, Response response) -> {
        return Arrays.stream(catalogService.listLightingTypes())
                .map(v -> LightingTypeDTO.builder()
                        .id(v.ordinal())
                        .code(v.name())
                        .description(v.type())
                        .build()
                ).collect(Collectors.toList());
    };

    private final Route fetchMarkIndicators = (Request request, Response response) -> {
        return Arrays.stream(catalogService.markIndicatorsTypes())
                .map(v -> MarkIndicatorTypeDTO.builder()
                            .id(v.ordinal())
                            .code(v.name())
                            .description(v.type())
                            .build()
                )
                .collect(Collectors.toList());
    };

    private final Route fetchObjectTypes = (Request request, Response response) -> {
        return Arrays.stream(catalogService.fetchObjectTypes())
                .map(v -> PlacedObjectTypeDTO.builder()
                            .id(v.ordinal())
                            .code(v.name())
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
