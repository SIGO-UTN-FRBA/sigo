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

import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

public class PlacedObjectRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private PlacedObjectService objectService;
    private PlacedObjectTranslator translator;
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
        this.translator = objectTranslator;
        this.objectMapper = objectMapper;
    }

    private final Route fetchObjects = doInTransaction(false, (request, response) -> {
        return objectService.find(request.queryMap())
                .stream()
                .map(translator::getAsDTO)
                .collect(toList());
    });

    private final Route fetchObject = doInTransaction(false, (request, response) -> {

        PlacedObject domain = objectService.get(getParamObjectId(request));

        return translator.getAsDTO(domain);
    });

    private final Route updateObject = doInTransaction(true, (request, response) -> {

        PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);

        PlacedObject domain = translator.getAsDomain(dto);

        objectService.update(domain);

        return translator.getAsDTO(domain);
    });

    private final Route createObject = doInTransaction(true, (request, response) -> {

        PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);

        PlacedObject domain = translator.getAsDomain(dto);

        objectService.create(domain);

        return translator.getAsDTO(domain);
    });

    private final Route deleteObject = doInTransaction(true, (request, response) -> {

        PlacedObject domain = objectService.get(getParamObjectId(request));

        objectService.delete(domain.getId());

        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    });


    private final Route fetchGeometry = doInTransaction(false, (request, response) -> {

        PlacedObject domain = objectService.get(getParamObjectId(request));

        return domain.getSpecification().getGeom();
    });

    @Override
    public RouteGroup routes() {
        return ()-> {

            get("", fetchObjects, jsonTransformer);
            post("", createObject, jsonTransformer);

            get("/:" + OBJECT_ID_PARAM, fetchObject, jsonTransformer);
            put("/:" + OBJECT_ID_PARAM, updateObject, jsonTransformer);
            delete("/:" + OBJECT_ID_PARAM, deleteObject);

            get("/:" + OBJECT_ID_PARAM +"/geometry", fetchGeometry, jsonTransformer);
            //post("/:" + OBJECT_ID_PARAM +"/geom". defineGeometry, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/objects";
    }
}
