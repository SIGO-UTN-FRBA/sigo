package ar.edu.utn.frba.proyecto.sigo.router.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.LightingTypes;
import ar.edu.utn.frba.proyecto.sigo.dto.LightingTypeDTO;
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
                        .map(l -> LightingTypeDTO.builder()
                                .id(l.ordinal())
                                .code(l.name())
                                .description(l.type())
                                .build()
                        ).collect(Collectors.toList());
    };

    @Override
    public RouteGroup routes() {
        return () -> {
            get("/lightings", fetchLightings, jsonTransformer);

        };
    }

    @Override
    public String path() {
        return "/catalogs/objects";
    }
}
