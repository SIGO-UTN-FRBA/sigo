package ar.edu.utn.frba.proyecto.sigo.router.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayStrip;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayDTO;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.SimpleFeatureTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayStripService;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.airport.AirportService;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Polygon;
import org.eclipse.jetty.http.HttpStatus;
import org.opengis.feature.simple.SimpleFeature;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

public class RunwayRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private RunwayService runwayService;
    private AirportService airportService;
    private RunwayTranslator translator;
    private SimpleFeatureTranslator featureTranslator;

    @Inject
    public RunwayRouter(
        Gson objectMapper,
        HibernateUtil hibernateUtil,
        JsonTransformer jsonTransformer,
        RunwayService runwayService,
        AirportService airportService,
        RunwayTranslator runwayTranslator,
        SimpleFeatureTranslator featureTranslator
    ){
        super(objectMapper, hibernateUtil);

        this.jsonTransformer = jsonTransformer;
        this.runwayService = runwayService;
        this.airportService = airportService;
        this.translator = runwayTranslator;
        this.featureTranslator = featureTranslator;
    }

    /**
     * Get runways related to an airport
     */
    private final Route fetchRunways = doInTransaction(false, (Request request, Response response) ->{

        Airport airport = airportService.get(getParamAirportId(request));

        return airportService.getRunways(airport)
                .stream()
                .map(translator::getAsDTO)
                .collect(toList());
    });

    /**
     * Get a runway given its identifier
     */
    private final Route fetchRunway = doInTransaction(false, (Request request, Response response) -> {

        Runway runway = runwayService.get(getParamRunwayId(request));

        return translator.getAsDTO(runway);
    });

    /**
     * Create a runway for a given airport
     */
    private final Route createRunway = doInTransaction(true, (Request request, Response response) -> {

        RunwayDTO runwayDTO = translator.getAsDTO(request.body());

        Airport airport = airportService.get(getParamAirportId(request));

        Runway runway = translator.getAsDomain(runwayDTO);

        runwayService.create(runway, airport);

        return translator.getAsDTO(runway);
    });

    /**
     * Update runway's geometry (polygon)
     */
    private final Route updateFeature = doInTransaction(true, (Request request, Response response) -> {

        Runway runway = runwayService.get(getParamRunwayId(request));

        SimpleFeature feature = featureTranslator.getAsDomain(objectMapper.fromJson(request.body(), JsonObject.class));

        runwayService.updateGeometry((Polygon)feature.getDefaultGeometry(), runway);

        return runway.getGeom();
    });

    /**
     * Get runway as feature
     */
    private final Route fetchFeature = doInTransaction(false, (Request request, Response response) -> {

        Runway runway = runwayService.get(getParamRunwayId(request));

        return featureTranslator.getAsDTO(runwayService.getFeature(runway));
    });

    /**
     * Update runway's properties
     */
    private final Route updateRunway = doInTransaction(true, (Request request, Response response) -> {

        RunwayDTO runwayDTO = objectMapper.fromJson(request.body(), RunwayDTO.class);

        Runway runway = translator.getAsDomain(runwayDTO);

        return translator.getAsDTO(runwayService.update(runway));
    });

    /**
     * Delete a runway by id
     */
    private final Route deleteRunway = doInTransaction(true, (Request request, Response response) -> {

        runwayService.delete(getParamRunwayId(request));

        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    });

    @SuppressWarnings("Duplicates")
    @Override
    public RouteGroup routes() {
        return () -> {
            get("", fetchRunways, jsonTransformer);
            post("", createRunway, jsonTransformer);

            get(format("/:%s", RUNWAY_ID_PARAM), fetchRunway, jsonTransformer);
            put(format("/:%s", RUNWAY_ID_PARAM), updateRunway, jsonTransformer);
            delete(format("/:%s", RUNWAY_ID_PARAM), deleteRunway);

            get(format("/:%s/feature", RUNWAY_ID_PARAM), fetchFeature, jsonTransformer);
            patch(format("/:%s/feature", RUNWAY_ID_PARAM), updateFeature, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/airports/:"+ AIRPORT_ID_PARAM +"/runways";
    }
}
