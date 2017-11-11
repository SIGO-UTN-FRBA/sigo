package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.geotools.geojson.feature.FeatureJSON;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.opengis.feature.simple.SimpleFeature;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.BiFunction;

public abstract class SigoRouter extends Router {

    protected static String AIRPORT_ID_PARAM = "airport_id";
    protected static String RUNWAY_ID_PARAM = "runway_id";
    protected static String RUNWAY_DIRECTION_ID_PARAM = "direction_id";
    protected static String REGION_ID_PARAM = "region_id";
    protected static String OBJECT_ID_PARAM = "object_id";
    protected static String OWNER_ID_PARAM = "owner_id";
    protected static String LOCATION_ID_PARAM = "location_id";
    protected static String SURFACE_ID_PARAM = "surface_id";
    protected static String REGULATION_ID_PARAM="regulation_id";
    protected static String ANALYSIS_ID_PARAM ="analysis_id";

    protected Gson objectMapper;
    @Getter
    public HibernateUtil hibernateUtil;

    public SigoRouter(Gson objectMapper, HibernateUtil hibernateUtil) {
        this.objectMapper = objectMapper;
        this.hibernateUtil = hibernateUtil;
    }

    protected Long getParamAirportId(Request request){
        return getParam(request, AIRPORT_ID_PARAM);
    }

    protected Long getParamRunwayId(Request request){
        return getParam(request, RUNWAY_ID_PARAM);
    }

    protected Long getParamDirectionId(Request request) {
        return getParam(request, RUNWAY_DIRECTION_ID_PARAM);
    }

    protected Long getParamRegionId(Request request){
        return getParam(request, REGION_ID_PARAM);
    }

    protected Long getParamObjectId(Request request) {
        return getParam(request, OBJECT_ID_PARAM);
    }

    protected Long getParamOwnerId(Request request) {
        return getParam(request, OWNER_ID_PARAM);
    }

    protected Long getParamLocationId(Request request){
        return getParam(request, LOCATION_ID_PARAM);
    }

    protected Long getParamSurfaceId(Request request){
        return getParam(request, SURFACE_ID_PARAM);
    }

    protected Long getParamRegulationId(Request request){
        return getParam(request, REGULATION_ID_PARAM);
    }

    protected Long getParamAnalysisId(Request request){
        return getParam(request, ANALYSIS_ID_PARAM);
    }

    private Long getParam(Request request, String key) {
        return Optional
                .ofNullable(request.params(key))
                .map(Long::valueOf)
                .orElseThrow(() -> new MissingParameterException(key));
    }


    protected <R> Route doInTransaction(Boolean inTransaction, BiFunction<Request, Response, R> route){

        return (Request request, Response response) ->{

            SessionFactory sessionFactory = this.getHibernateUtil().getSessionFactory();

            Session session = sessionFactory.openSession();

            try {
                //configureSession(session);

                ManagedSessionContext.bind(session);

                if(inTransaction) session.beginTransaction();

                try {

                    R r = route.apply(request, response);

                    if(inTransaction) commitTransaction(session);

                    return r;

                } catch (SigoException e){

                    if(inTransaction) abortTransaction(session);

                    e.printStackTrace();

                    throw e;

                }catch (Exception e) {

                    if(inTransaction) abortTransaction(session);

                    e.printStackTrace();

                    throw new SigoException(e);
                }
            } finally {

                session.close();

                ManagedSessionContext.unbind(sessionFactory);
            }
        };
    }

    private void abortTransaction(Session session) {
        Transaction txn = session.getTransaction();
        if (txn != null && txn.getStatus() == TransactionStatus.ACTIVE) {
            txn.rollback();
        }
    }

    private void commitTransaction(Session session) {
        Transaction txn = session.getTransaction();

        if (txn != null && txn.getStatus() == TransactionStatus.ACTIVE) {
            txn.commit();
        }
    }

    protected JsonObject featureToGeoJson(SimpleFeature feature) {

        try(OutputStream outputStream = new ByteArrayOutputStream()) {

            new FeatureJSON().writeFeature(feature, outputStream);

            return objectMapper.fromJson(outputStream.toString(),JsonObject.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new SigoException(e);
        }
    }

    protected SimpleFeature featureFromGeoJson(String json) {

        try (InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8.name()))){

            return new FeatureJSON().readFeature(stream);

        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidParameterException("Malformed geometry.",e);
        }
    }
}
