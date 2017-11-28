package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisExceptionDTO;
import ar.edu.utn.frba.proyecto.sigo.dto.common.ListItemDTO;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisExceptionService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisExceptionTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.http.HttpStatus;
import spark.Route;
import spark.RouteGroup;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;

import java.util.stream.Collectors;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class AnalysisExceptionRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisService analysisService;
    private AnalysisExceptionTranslator translator;
    private AnalysisExceptionService exceptionService;

    @Inject
    public AnalysisExceptionRouter(
            HibernateUtil hibernateUtil,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            AnalysisService analysisService,
            AnalysisExceptionService exceptionService,
            AnalysisExceptionTranslator translator
    ){
        super(objectMapper, hibernateUtil);

        this.jsonTransformer = jsonTransformer;
        this.analysisService = analysisService;
        this.exceptionService = exceptionService;
        this.translator = translator;
    }


    /**
     * Get exceptions by case
     */
    private final Route fetchExceptions = doInTransaction(false, (request, response) -> {

        Analysis analysis = this.analysisService.get(this.getParamAnalysisId(request));

        return analysis.getAnalysisCase().getExceptions()
                .stream()
                .map(e -> translator.getAsAbstractDTO(e))
                .collect(Collectors.toList());
    });

    /**
     * Create an exception for an analysis case
     */
    private final Route createException = doInTransaction(true, (request, response) -> {

        AnalysisExceptionDTO dto = translator.getAsDTO(request.body());

        AnalysisException domain = translator.getAsDomain(dto);

        this.exceptionService.create(domain);

        return translator.getAsDTO(domain);
    });

    /**
     * Get an exception for an analysis case
     */
    private final Route fetchException = doInTransaction(false, (request, response) -> {
        AnalysisException exception = this.exceptionService.get(this.getParamExceptionId(request));

        return translator.getAsDTO(exception);
    });

    /**
     * Update an exception for an analysis case
     */
    private final Route updateException = doInTransaction(true, (request, response) -> {
        throw new NotImplementedException();
    });

    /**
     * Delete an exception for an analysis case
     */
    private final Route deleteException = doInTransaction(true, (request, response) -> {

        exceptionService.delete(this.getParamExceptionId(request));

        response.status(HttpStatus.NO_CONTENT_204);

        response.body("");

        return response.body();
    });

    @Override
    public RouteGroup routes() {
        return () -> {

            //TODO validar que se encuentre en stage correcto

            get("", fetchExceptions, jsonTransformer);
            post("", createException, jsonTransformer);

            get("/:" + EXCEPTION_ID_PARAM, fetchException, jsonTransformer);
            put("/:" + EXCEPTION_ID_PARAM, updateException, jsonTransformer);
            delete("/:" + EXCEPTION_ID_PARAM, deleteException, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/:" + ANALYSIS_ID_PARAM + "/case/exceptions" ;
    }
}
