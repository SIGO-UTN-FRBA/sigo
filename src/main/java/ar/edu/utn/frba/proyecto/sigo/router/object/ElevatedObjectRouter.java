package ar.edu.utn.frba.proyecto.sigo.router.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.*;
import ar.edu.utn.frba.proyecto.sigo.dto.object.PlacedObjectDTO;
import ar.edu.utn.frba.proyecto.sigo.dto.object.TrackSectionDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.object.*;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.translator.SimpleFeatureTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.object.PlacedObjectTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.object.TerrainLevelCurveTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.object.TrackSectionTranslator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.lang.NotImplementedException;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.SessionFactory;
import org.opengis.feature.simple.SimpleFeature;
import spark.QueryParamsMap;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import java.util.Optional;

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
    private TrackSectionService trackSectionService;
    private TrackSectionFeatureHelper trackSectionHelper;
    private TrackSectionTranslator trackSectionTranslator;

    @Inject
    public ElevatedObjectRouter(
            SessionFactory sessionFactory,
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
            TerrainLevelCurveTranslator terrainTranslator,
            TrackSectionService trackSectionService,
            TrackSectionFeatureHelper trackSectionHelper,
            TrackSectionTranslator trackSectionTranslator
    ){
        super(objectMapper, sessionFactory);

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
        this.trackSectionService = trackSectionService;
        this.trackSectionHelper = trackSectionHelper;
        this.trackSectionTranslator = trackSectionTranslator;
    }

    private final Route fetchObjects = doInTransaction(false, (request, response) -> {

        Integer typeId = Optional.ofNullable(request.queryMap().get("type"))
                .map(QueryParamsMap::integerValue)
                .orElseThrow(() -> new MissingParameterException("type"));

        switch(ElevatedObjectTypes.values()[typeId]){

            case BUILDING:{
                return buildingService.find(request.queryMap())
                        .map(placedObjectTranslator::getAsDTO)
                        .collect(toList());
            }
            case INDIVIDUAL: {
                return individualService.find(request.queryMap())
                        .map(placedObjectTranslator::getAsDTO)
                        .collect(toList());
            }
            case OVERHEAD_WIRED:{
                return overheadWireService.find(request.queryMap())
                        .map(placedObjectTranslator::getAsDTO)
                        .collect(toList());
            }
            case TRACK_SECTION:{
                return trackSectionService.find(request.queryMap())
                        .map(trackSectionTranslator::getAsDTO)
                        .collect(toList());
            }

            case LEVEL_CURVE:
                break;
        }

        throw new NotImplementedException();
    });

    private final Route fetchObject = doInTransaction(false, (request, response) -> {

        Long objectId = getParamObjectId(request);

        Integer typeId = Optional.ofNullable(request.queryMap().get("type"))
                .map(QueryParamsMap::integerValue)
                .orElseThrow(() -> new MissingParameterException("type"));

        switch(ElevatedObjectTypes.values()[typeId]){

            case BUILDING: {
                return placedObjectTranslator.getAsDTO(buildingService.get(objectId));
            }
            case INDIVIDUAL: {
                return placedObjectTranslator.getAsDTO(individualService.get(objectId));
            }
            case OVERHEAD_WIRED: {
                return placedObjectTranslator.getAsDTO(overheadWireService.get(objectId));
            }
            case LEVEL_CURVE:{
                return terrainTranslator.getAsDTO(terrainService.get(objectId));
            }
            case TRACK_SECTION:{
                return trackSectionTranslator.getAsDTO(trackSectionService.get(objectId));
            }
        }

        throw new NotImplementedException();
    });

    private final Route updateObject = doInTransaction(true, (request, response) -> {

        Integer typeId = Optional.ofNullable(request.queryMap().get("type"))
                .map(QueryParamsMap::integerValue)
                .orElseThrow(() -> new MissingParameterException("type"));


        switch(ElevatedObjectTypes.values()[typeId]){

            case BUILDING: {
                PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);
                PlacedObject domain = placedObjectTranslator.getAsBuildingDomain(dto);
                buildingService.update((PlacedObjectBuilding) domain);
                return placedObjectTranslator.getAsDTO(domain);
            }
            case INDIVIDUAL: {
                PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);
                PlacedObject domain = placedObjectTranslator.getAsIndividualDomain(dto);
                individualService.update((PlacedObjectIndividual) domain);
                return placedObjectTranslator.getAsDTO(domain);
            }
            case OVERHEAD_WIRED: {
                PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);
                PlacedObject domain = placedObjectTranslator.getAsWiredDomain(dto);
                overheadWireService.update((PlacedObjectOverheadWire) domain);
                return placedObjectTranslator.getAsDTO(domain);
            }
            case TRACK_SECTION: {
                TrackSectionDTO dto = trackSectionTranslator.getAsDTO(request.body());
                TrackSection domain = trackSectionTranslator.getAsDomain(dto);
                trackSectionService.update(domain);
                return trackSectionTranslator.getAsDTO(domain);
            }
        }

        throw new NotImplementedException();
    });

    private final Route createObject = doInTransaction(true, (request, response) -> {

        Integer typeId = Optional.ofNullable(request.queryMap().get("type"))
                .map(QueryParamsMap::integerValue)
                .orElseThrow(() -> new MissingParameterException("type"));

        switch(ElevatedObjectTypes.values()[typeId]){

            case BUILDING:{
                PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);
                PlacedObjectBuilding domain = placedObjectTranslator.getAsBuildingDomain(dto);
                buildingService.create(domain);
                return placedObjectTranslator.getAsDTO(domain);
            }
            case INDIVIDUAL:{
                PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);
                PlacedObjectIndividual domain = placedObjectTranslator.getAsIndividualDomain(dto);
                individualService.create(domain);
                return placedObjectTranslator.getAsDTO(domain);
            }
            case OVERHEAD_WIRED:{
                PlacedObjectDTO dto = objectMapper.fromJson(request.body(), PlacedObjectDTO.class);
                PlacedObjectOverheadWire domain = placedObjectTranslator.getAsWiredDomain(dto);
                overheadWireService.create(domain);
                return placedObjectTranslator.getAsDTO(domain);
            }
            case TRACK_SECTION:{
                TrackSectionDTO dto = trackSectionTranslator.getAsDTO(request.body());
                TrackSection domain = trackSectionTranslator.getAsDomain(dto);
                trackSectionService.create(domain);
                return trackSectionTranslator.getAsDTO(domain);
            }
        }

        throw new NotImplementedException();
    });

    private final Route deleteObject = doInTransaction(true, (request, response) -> {

        Integer typeId = Optional.ofNullable(request.queryMap().get("type"))
                .map(QueryParamsMap::integerValue)
                .orElseThrow(() -> new MissingParameterException("type"));

        Long objectId = getParamObjectId(request);

        switch (ElevatedObjectTypes.values()[typeId]) {
            case BUILDING: case INDIVIDUAL:case OVERHEAD_WIRED:{
                placedObjectService.delete(objectId);
                break;
            }
            case TRACK_SECTION:{
                trackSectionService.delete(objectId);
                break;
            }
        }


        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    });

    /**
     * Get object as feature
     */
    private final Route fetchFeature = doInTransaction(false, (request, response) -> {

        Long objectId = getParamObjectId(request);

        Integer typeId = Optional.ofNullable(request.queryMap().get("type"))
                .map(QueryParamsMap::integerValue)
                .orElseThrow(() -> new MissingParameterException("type"));

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
            case TRACK_SECTION:{
                return featureTranslator.getAsDTO(
                    trackSectionHelper.getFeature(trackSectionService.get(objectId))
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

        Integer typeId = Optional.ofNullable(request.queryMap().get("type"))
                .map(QueryParamsMap::integerValue)
                .orElseThrow(() -> new MissingParameterException("type"));

        SimpleFeature feature = featureTranslator.getAsDomain(objectMapper.fromJson(request.body(), JsonObject.class));

        switch(ElevatedObjectTypes.values()[typeId]){

            case BUILDING: {
                PlacedObject domain = buildingService.get(objectId);
                placedObjectService.updateGeometry((Geometry)feature.getDefaultGeometry(), domain);
                return domain.getGeom();
            }
            case INDIVIDUAL: {
                PlacedObject domain = individualService.get(objectId);
                placedObjectService.updateGeometry((Point)feature.getDefaultGeometry(), domain);
                return domain.getGeom();
            }
            case OVERHEAD_WIRED: {
                PlacedObject domain = overheadWireService.get(objectId);
                placedObjectService.updateGeometry((LineString)feature.getDefaultGeometry(), domain);
                return domain.getGeom();
            }
            case TRACK_SECTION: {
                TrackSection domain = trackSectionService.get(objectId);
                trackSectionService.updateGeometry((LineString)feature.getDefaultGeometry(), domain);
                return domain.getGeom();
            }
        }

        throw new NotImplementedException();
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
