package ar.edu.utn.frba.proyecto.sigo.router.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.dto.common.EnumerationDTO;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class RegulationRouter extends SigoRouter {

    JsonTransformer jsonTransformer;

    @Inject
    public RegulationRouter(
            JsonTransformer jsonTransformer
    ){
        this.jsonTransformer = jsonTransformer;
    }

    private final Route fetchRegulations = (request, response) -> {
        return Arrays.stream(Regulations.values())
                .map(r -> new EnumerationDTO(r.ordinal(), r.name(), r.description()))
                .collect(Collectors.toList());
    };

    @Override
    public RouteGroup routes() {
        return ()-> {
            get("", fetchRegulations, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/regulations";
    }
}
