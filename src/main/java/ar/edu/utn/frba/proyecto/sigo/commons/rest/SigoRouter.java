package ar.edu.utn.frba.proyecto.sigo.commons.rest;

import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.gson.Gson;
import spark.Request;

import java.util.Optional;

public abstract class SigoRouter extends Router {

    protected static String AIRPORT_ID_PARAM = "airport_id";
    protected static String RUNWAY_ID_PARAM = "runway_id";
    protected static String RUNWAY_DIRECTION_ID_PARAM = "direction_id";

    protected Gson objectMapper;


    protected Long getParamAirportId(Request request){
        return getParam(request, AIRPORT_ID_PARAM);
    }

    protected Long getParamRunwayId(Request request){
        return getParam(request, RUNWAY_ID_PARAM);
    }


    protected Long getParamDirectionId(Request request) {
        return getParam(request, RUNWAY_DIRECTION_ID_PARAM);
    }

    private Long getParam(Request request, String key) {
        return Optional
                .ofNullable(request.params(key))
                .map(Long::valueOf)
                .orElseThrow(() -> new MissingParameterException(key));
    }
}
