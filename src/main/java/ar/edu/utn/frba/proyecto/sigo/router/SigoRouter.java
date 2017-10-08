package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.gson.Gson;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Optional;
import java.util.function.BiFunction;

public abstract class SigoRouter extends Router {

    protected static String AIRPORT_ID_PARAM = "airport_id";
    protected static String RUNWAY_ID_PARAM = "runway_id";
    protected static String RUNWAY_DIRECTION_ID_PARAM = "direction_id";
    protected static String REGION_ID_PARAM = "region_id";

    protected Gson objectMapper;

    @Getter
    public HibernateUtil hibernateUtil;

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
}
