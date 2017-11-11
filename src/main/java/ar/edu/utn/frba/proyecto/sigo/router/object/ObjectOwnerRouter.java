package ar.edu.utn.frba.proyecto.sigo.router.object;

import ar.edu.utn.frba.proyecto.sigo.dto.common.ListItemDTO;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.object.ObjectOwnerService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.stream.Collectors;

import static spark.Spark.get;

public class ObjectOwnerRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private ObjectOwnerService objectService;

    @Inject
    public ObjectOwnerRouter(
            HibernateUtil hibernateUtil,
            JsonTransformer jsonTransformer,
            ObjectOwnerService objectService
    ) {
        super(null, hibernateUtil);

        this.jsonTransformer = jsonTransformer;
        this.objectService = objectService;
    }

    private final Route fetchOwners = doInTransaction(false, (request, response) -> {
        return objectService.findAll()
                    .stream()
                    .map(o -> new ListItemDTO(o.getId(), o.getName()))
                    .collect(Collectors.toList());
    });

    private final Route fetchOwner = doInTransaction(false, (request, response) -> {
        return objectService.get(getParamOwnerId(request));
    });

    @Override
    public RouteGroup routes() {
        return () -> {
            get("", fetchOwners, jsonTransformer);
            get("/:" + OWNER_ID_PARAM, fetchOwner, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/owners";
    }
}
