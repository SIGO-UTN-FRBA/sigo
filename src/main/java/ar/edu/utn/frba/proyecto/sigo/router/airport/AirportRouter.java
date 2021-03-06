package ar.edu.utn.frba.proyecto.sigo.router.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.AirportDTO;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.airport.AirportService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.translator.SimpleFeatureTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.airport.AirportTranslator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Point;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.SessionFactory;
import org.opengis.feature.simple.SimpleFeature;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

@Singleton
public class AirportRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AirportService airportService;
    private AirportTranslator translator;
    private SimpleFeatureTranslator featureTranslator;

    @Inject
    public AirportRouter(
            SessionFactory sessionFactory,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            AirportService airportService,
            AirportTranslator translator,
            SimpleFeatureTranslator featureTranslator
    ) {
        super(objectMapper, sessionFactory);

        this.jsonTransformer = jsonTransformer;
        this.airportService = airportService;
        this.translator = translator;
        this.featureTranslator = featureTranslator;
    }

    /**
     * Get a list of airports instances filtered by values of its properties
     */
    private final Route fetchAirports = doInTransaction(false, (Request request, Response response) -> {

        Stream<Airport> airports;

        if(request.queryMap().hasKey("withoutCases")) {
            airports = airportService.findWithoutAnalysis();
        } else {
            airports = airportService.find(request.queryMap());
        }

        return airports.map(translator::getAsDTO).collect(toList());
    });

    /**
     * Update airport's properties
     */
    private final Route updateAirport = doInTransaction(true, (Request request, Response response) -> {

        AirportDTO dto = translator.getAsDTO(request.body());

        Airport airport = translator.getAsDomain(dto);

        return translator.getAsDTO(airportService.update(airport));
    });

    /**
     * Create an airport
     */
    private final Route createAirport = doInTransaction(true, (Request request, Response response) -> {

        AirportDTO dto = translator.getAsDTO(request.body());

        Airport airport = translator.getAsDomain(dto);

        airportService.create(airport);

        return translator.getAsDTO(airport);
    });

    /**
     * Delete an airport given its identifier
     */
    private final Route deleteAirport = doInTransaction(true, (Request request, Response response) -> {

        airportService.delete(getParamAirportId(request));

        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    });

    /**
     * Get an airport given its identifier
     */
    private final Route fetchAirport = doInTransaction(false, (Request request, Response response) -> {

        Airport airport = airportService.get(getParamAirportId(request));

        return translator.getAsDTO(airport);
    });

    /**
     * Get airport as feature
     */
    private final Route fetchFeature = doInTransaction(false, (Request request, Response response) -> {

        Airport airport = airportService.get(getParamAirportId(request));

        return featureTranslator.getAsDTO(airportService.getFeature(airport));

    });

    /**
     * Update airport's geometry (point)
     */
    private final Route updateFeature = doInTransaction(true, (Request request, Response response) -> {

        Airport airport = airportService.get(getParamAirportId(request));

        SimpleFeature feature = featureTranslator.getAsDomain(objectMapper.fromJson(request.body(), JsonObject.class));

        airportService.updateGeometry((Point)feature.getDefaultGeometry(), airport);

        return airport.getGeom();
    });


    @SuppressWarnings("Duplicates")
    @Override
    public RouteGroup routes() {
        return ()-> {
            get("", fetchAirports, jsonTransformer);
            post("", createAirport,jsonTransformer);

            get(format("/:%s", AIRPORT_ID_PARAM), fetchAirport, jsonTransformer);
            put(format("/:%s", AIRPORT_ID_PARAM), updateAirport, jsonTransformer);
            delete(format("/:%s", AIRPORT_ID_PARAM), deleteAirport);

            get(format("/:%s/feature", AIRPORT_ID_PARAM), fetchFeature, jsonTransformer);
            patch(format("/:%s/feature", AIRPORT_ID_PARAM), updateFeature, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/airports";
    }
}
