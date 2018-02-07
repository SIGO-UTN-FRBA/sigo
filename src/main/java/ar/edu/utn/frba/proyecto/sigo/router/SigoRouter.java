package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.exception.InternalServerErrorException;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import ar.edu.utn.frba.proyecto.sigo.security.UserSession;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.gson.Gson;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.persistence.EntityTransaction;
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
    protected static String EXCEPTION_ID_PARAM ="exception_id";
    protected static String RULE_ID_PARAM = "rule_id";
    protected static String OBSTACLE_ID_PARAM = "obstacle_id";

    protected Gson objectMapper;

    @Getter
    public SessionFactory sessionFactory;

    public SigoRouter(Gson objectMapper, SessionFactory sessionFactory) {
        this.objectMapper = objectMapper;
        this.sessionFactory = sessionFactory;
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

    protected Long getParamExceptionId(Request request){
        return getParam(request, EXCEPTION_ID_PARAM);
    }

    protected Long getParamRuleId(Request request){
        return getParam(request, RULE_ID_PARAM);
    }

    protected Long getParamObstacleId(Request request){
        return getParam(request, OBSTACLE_ID_PARAM);
    }

    private Long getParam(Request request, String key) {
        return Optional
                .ofNullable(request.params(key))
                .map(Long::valueOf)
                .orElseThrow(() -> new MissingParameterException(key));
    }

    protected <R> Route doInTransaction(Boolean inTransaction, BiFunction<Request, Response, R> route){

        return (Request request, Response response) ->{

            try (Session session = sessionFactory.openSession()) {
                //configureSession(session);

                ManagedSessionContext.bind(session);

                Optional<Transaction> transaction = (inTransaction) ? Optional.of(session.beginTransaction()) : Optional.empty();

                try {

                    R r = route.apply(request, response);

                    transaction.ifPresent(EntityTransaction::commit);

                    return r;

                } catch (SigoException e) {

                    transaction.ifPresent(EntityTransaction::rollback);

                    e.printStackTrace();

                    throw e;

                } catch (Exception e) {

                    transaction.ifPresent(EntityTransaction::rollback);

                    e.printStackTrace();

                    throw new InternalServerErrorException(e);
                }
            } finally {
                ManagedSessionContext.unbind(sessionFactory);
            }
        };
    }

    protected UserSession getCurrentUserSession(Request request) {
        return request.attribute("current-session");
    }
}
