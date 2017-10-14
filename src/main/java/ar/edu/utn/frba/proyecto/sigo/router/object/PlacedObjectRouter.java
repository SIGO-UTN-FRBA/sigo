package ar.edu.utn.frba.proyecto.sigo.router.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.dto.object.PlacedObjectDTO;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.object.PlacedObjectService;
import ar.edu.utn.frba.proyecto.sigo.service.object.PlacedObjectTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import org.eclipse.jetty.http.HttpStatus;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static spark.Spark.*;

public class PlacedObjectRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private PlacedObjectService objectService;
    private PlacedObjectTranslator objectTranslator;
    private Gson objectMapper;

    @Inject
    public PlacedObjectRouter(
        HibernateUtil hibernateUtil,
        JsonTransformer jsonTransformer,
        PlacedObjectService objectService,
        PlacedObjectTranslator objectTranslator,
        Gson objectMapper
    ){
        this.hibernateUtil = hibernateUtil;
        this.jsonTransformer = jsonTransformer;
        this.objectService = objectService;
        this.objectTranslator = objectTranslator;
        this.objectMapper = objectMapper;
    }

    private final Route fetchObject = doInTransaction(false, (request, response) -> {

        PlacedObject domain = objectService.get(getParamObjectId(request));

        return objectTranslator.getAsDTO(domain);
    });

    private final Route updateObject = doInTransaction(true, (request, response) -> {

        PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);

        PlacedObject domain = objectTranslator.getAsDomain(dto);

        objectService.update(domain);

        return objectTranslator.getAsDTO(domain);
    });

    private final Route deleteObject = doInTransaction(true, (request, response) -> {

        PlacedObject domain = objectService.get(getParamObjectId(request));

        objectService.delete(domain.getId());

        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    });



    @Override
    public RouteGroup routes() {
        return ()-> {

            //get("", fetchObjects, jsonTransformer);
            //post("", saveObject, jsonTransformer);

            get("/:" + OBJECT_ID_PARAM, fetchObject, jsonTransformer);
            put("/:" + OBJECT_ID_PARAM, updateObject, jsonTransformer);
            delete("/:" + OBJECT_ID_PARAM, deleteObject);

            //get("/:" + OBJECT_ID_PARAM +"/geom". fetchGeometry, jsonTransformer);
            //post("/:" + OBJECT_ID_PARAM +"/geom". defineGeometry, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/objects";
    }
}
