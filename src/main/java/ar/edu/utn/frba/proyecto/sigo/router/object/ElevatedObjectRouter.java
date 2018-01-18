package ar.edu.utn.frba.proyecto.sigo.router.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.*;
import ar.edu.utn.frba.proyecto.sigo.dto.object.PlacedObjectDTO;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.object.*;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.translator.SimpleFeatureTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.object.PlacedObjectTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.object.TerrainLevelCurveTranslator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.lang.NotImplementedException;
import org.eclipse.jetty.http.HttpStatus;
import org.opengis.feature.simple.SimpleFeature;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

public class ElevatedObjectRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private PlacedObjectService placedObjectService;
    private PlacedObjectTranslator placedObjectTranslator;
    private PlacedObjectBuildingService buildingService;
    private PlacedObjectIndividualService individualService;
    private PlacedObjectOverheadWireService overheadWireService;
    private SimpleFeatureTranslator featureTranslator;
    private PlacedObjectFeatureHelper featurePlacedObjectHelper;
    private TerrainLevelCurveService terrainService;
    private TerrainLevelCurveFeatureHelper featureTerrainHelper;
    private TerrainLevelCurveTranslator terrainTranslator;

    @Inject
    public ElevatedObjectRouter(
            HibernateUtil hibernateUtil,
            JsonTransformer jsonTransformer,
            PlacedObjectService placedObjectService,
            PlacedObjectTranslator objectTranslator,
            PlacedObjectFeatureHelper featurePlacedObjectHelper,
            Gson objectMapper,
            PlacedObjectBuildingService buildingService,
            PlacedObjectIndividualService individualService,
            PlacedObjectOverheadWireService overheadWireService,
            SimpleFeatureTranslator featureTranslator,
            TerrainLevelCurveService terrainService,
            TerrainLevelCurveFeatureHelper featureTerrainHelper,
            TerrainLevelCurveTranslator terrainTranslator){
        super(objectMapper, hibernateUtil);

        this.featurePlacedObjectHelper = featurePlacedObjectHelper;
        this.jsonTransformer = jsonTransformer;
        this.placedObjectService = placedObjectService;
        this.placedObjectTranslator = objectTranslator;
        this.buildingService = buildingService;
        this.individualService = individualService;
        this.overheadWireService = overheadWireService;
        this.featureTranslator = featureTranslator;
        this.terrainService = terrainService;
        this.featureTerrainHelper = featureTerrainHelper;
        this.terrainTranslator = terrainTranslator;
    }

    private final Route fetchObjects = doInTransaction(false, (request, response) -> {

        Integer typeId = request.queryMap().get(PlacedObject_.type.getName()).integerValue();

        switch(ElevatedObjectTypes.values()[typeId]){

            case BUILDING: case INDIVIDUAL: case OVERHEAD_WIRED:{
                return placedObjectService.find(request.queryMap())
                        .map(placedObjectTranslator::getAsDTO)
                        .collect(toList());
            }
            case LEVEL_CURVE:
                break;
        }

        throw new NotImplementedException();
    });

    private final Route fetchObject = doInTransaction(false, (request, response) -> {

        Long objectId = getParamObjectId(request);

        Integer typeId = request.queryMap().get(PlacedObject_.type.getName()).integerValue();

        switch(ElevatedObjectTypes.values()[typeId]){

            case BUILDING: {
                return placedObjectTranslator.getAsDTO(
                    buildingService.get(objectId)
                );
            }
            case INDIVIDUAL: {
                return placedObjectTranslator.getAsDTO(
                    individualService.get(objectId)
                );
            }
            case OVERHEAD_WIRED: {
                return placedObjectTranslator.getAsDTO(
                    overheadWireService.get(objectId)
                );
            }
            case LEVEL_CURVE:{
                return terrainTranslator.getAsDTO(
                    terrainService.get(objectId)
                );
            }
        }

        throw new NotImplementedException();
    });

    private final Route updateObject = doInTransaction(true, (request, response) -> {

        PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);

        PlacedObject domain = placedObjectTranslator.getAsDomain(dto);

        switch(ElevatedObjectTypes.values()[dto.getTypeId()]){

            case BUILDING: {
                buildingService.update((PlacedObjectBuilding) domain);
                break;
            }
            case INDIVIDUAL: {
                individualService.update((PlacedObjectIndividual) domain);
                break;
            }
            case OVERHEAD_WIRED: {
                overheadWireService.update((PlacedObjectOverheadWire) domain);
                break;
            }
        }

        return placedObjectTranslator.getAsDTO(domain);
    });

    private final Route createObject = doInTransaction(true, (request, response) -> {

        PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);

        PlacedObject domain = placedObjectTranslator.getAsDomain(dto);

        placedObjectService.create(domain);

        return placedObjectTranslator.getAsDTO(domain);
    });

    private final Route deleteObject = doInTransaction(true, (request, response) -> {

        PlacedObject domain = placedObjectService.get(getParamObjectId(request));

        placedObjectService.delete(domain.getId());

        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    });

    /**
     * Get object as feature
     */
    private final Route fetchFeature = doInTransaction(false, (request, response) -> {

        Long objectId = getParamObjectId(request);

        Integer typeId = request.queryMap().get(PlacedObject_.type.getName()).integerValue();

        switch(ElevatedObjectTypes.values()[typeId]){

            case BUILDING: {
                return featureTranslator.getAsDTO(
                    featurePlacedObjectHelper.getFeature(buildingService.get(objectId))
                );
            }
            case INDIVIDUAL: {
                return featureTranslator.getAsDTO(
                    featurePlacedObjectHelper.getFeature(individualService.get(objectId))
                );
            }
            case OVERHEAD_WIRED: {
                return featureTranslator.getAsDTO(
                    featurePlacedObjectHelper.getFeature(overheadWireService.get(objectId))
                );
            }
            case LEVEL_CURVE:{
                return featureTranslator.getAsDTO(
                    featureTerrainHelper.getFeature(terrainService.get(objectId))
                );
            }
        }

        throw new NotImplementedException();
    });

    /**
     * Update object's geometry
     */
    private final Route updateFeature = doInTransaction(true, (request, response) -> {

        Long objectId = getParamObjectId(request);

        Integer typeId = request.queryMap().get(PlacedObject_.type.getName()).integerValue();

        PlacedObject placedObject;

        switch(ElevatedObjectTypes.values()[typeId]){

            case BUILDING: {
                placedObject = buildingService.get(objectId);
                break;
            }
            case INDIVIDUAL: {
                placedObject = individualService.get(objectId);
                break;
            }
            case OVERHEAD_WIRED: {
                placedObject = overheadWireService.get(objectId);
                break;
            }
            default:
                throw new NotImplementedException();
        }

        SimpleFeature feature = featureTranslator.getAsDomain(objectMapper.fromJson(request.body(), JsonObject.class));

        placedObjectService.updateGeometry((Geometry)feature.getDefaultGeometry(), placedObject);

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
