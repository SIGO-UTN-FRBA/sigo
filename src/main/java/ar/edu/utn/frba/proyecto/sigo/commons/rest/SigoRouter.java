package ar.edu.utn.frba.proyecto.sigo.commons.rest;

import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.gson.Gson;
import spark.Request;

import java.util.Optional;

public abstract class SigoRouter extends Router {

    protected static String AIRPORT_ID_PARAM = "airport_id";
    protected static String RUNWAY_ID_PARAM = "runway_id";

    protected Gson objectMapper;


    protected Long getParamAirportId(Request request){
        return Optional
                .ofNullable(request.params(AIRPORT_ID_PARAM))
                .map(Long::valueOf)
                .orElseThrow(() -> new MissingParameterException(AIRPORT_ID_PARAM));
    }

    protected Long getParamRunwayId(Request request){
        return Optional
                .ofNullable(request.params(RUNWAY_ID_PARAM))
                .map(Long::valueOf)
                .orElseThrow(() -> new MissingParameterException(RUNWAY_ID_PARAM));
    }
}
