package ar.edu.utn.frba.proyecto.sigo.router.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.dto.object.PlacedObjectDTO;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.SimpleFeatureTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.object.PlacedObjectFeatureService;
import ar.edu.utn.frba.proyecto.sigo.service.object.PlacedObjectService;
import ar.edu.utn.frba.proyecto.sigo.service.object.PlacedObjectTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Geometry;
import org.eclipse.jetty.http.HttpStatus;
import org.opengis.feature.simple.SimpleFeature;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

public class PlacedObjectRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private PlacedObjectService objectService;
    private PlacedObjectTranslator translator;
    private SimpleFeatureTranslator featureTranslator;
    private PlacedObjectFeatureService featureService;

    @Inject
    public PlacedObjectRouter(
        HibernateUtil hibernateUtil,
        JsonTransformer jsonTransformer,
        PlacedObjectService objectService,
        PlacedObjectTranslator objectTranslator,
        PlacedObjectFeatureService featureService,
        Gson objectMapper,
        SimpleFeatureTranslator featureTranslator
    ){
        super(objectMapper, hibernateUtil);

        this.featureService = featureService;
        this.jsonTransformer = jsonTransformer;
        this.objectService = objectService;
        this.translator = objectTranslator;
        this.featureTranslator = featureTranslator;
    }

    private final Route fetchObjects = doInTransaction(false, (request, response) -> {
        return objectService.find(request.queryMap())
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

    /**
     * Get object as feature
     */
    private final Route fetchFeature = doInTransaction(false, (request, response) -> {

        PlacedObject object = objectService.get(getParamObjectId(request));

        return featureTranslator.getAsDTO(featureService.getFeature(object));
    });

    /**
     * Update object's geometry
     */
    private final Route updateFeature = doInTransaction(true, (request, response) -> {

        PlacedObject placedObject = objectService.get(getParamObjectId(request));

        SimpleFeature feature = featureTranslator.getAsDomain(objectMapper.fromJson(request.body(), JsonObject.class));

        objectService.updateGeometry((Geometry)feature.getDefaultGeometry(), placedObject);

        return placedObject.getGeom();
    });

    @Override
    public RouteGroup routes() {
        return ()-> {

            get("", fetchObjects, jsonTransformer);
            post("", createObject, jsonTransformer);

            get("/:" + OBJECT_ID_PARAM, fetchObject, jsonTransformer);
            put("/:" + OBJECT_ID_PARAM, updateObject, jsonTransformer);
            delete("/:" + OBJECT_ID_PARAM, deleteObject);

            get("/:" + OBJECT_ID_PARAM +"/feature", fetchFeature, jsonTransformer);
            patch("/:" + OBJECT_ID_PARAM +"/feature", updateFeature, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/objects";
    }
}
