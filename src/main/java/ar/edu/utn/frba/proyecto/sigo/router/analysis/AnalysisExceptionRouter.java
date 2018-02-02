package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionRule;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionSurface;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisExceptionRuleDTO;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisExceptionSurfaceDTO;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisExceptionRuleService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisExceptionService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisExceptionSurfaceService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.translator.SimpleFeatureTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisExceptionRuleTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisExceptionSurfaceTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisExceptionTranslator;
import com.google.gson.Gson;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.SessionFactory;
import spark.Route;
import spark.RouteGroup;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class AnalysisExceptionRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisService analysisService;
    private AnalysisExceptionRuleTranslator ruleTranslator;
    private AnalysisExceptionSurfaceTranslator surfaceTranslator;
    private AnalysisExceptionTranslator translator;
    private AnalysisExceptionService exceptionService;
    private AnalysisExceptionRuleService ruleService;
    private AnalysisExceptionSurfaceService surfaceService;
    private SimpleFeatureTranslator featureTranslator;

    @Inject
    public AnalysisExceptionRouter(
            SessionFactory sessionFactory,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            AnalysisService analysisService,
            AnalysisExceptionSurfaceTranslator surfaceTranslator,
            AnalysisExceptionService exceptionService,
            AnalysisExceptionRuleTranslator ruleTranslator,
            AnalysisExceptionTranslator translator,
            AnalysisExceptionRuleService ruleService,
            AnalysisExceptionSurfaceService surfaceService,
            SimpleFeatureTranslator featureTranslator){
        super(objectMapper, sessionFactory);

        this.jsonTransformer = jsonTransformer;
        this.analysisService = analysisService;
        this.surfaceTranslator = surfaceTranslator;
        this.exceptionService = exceptionService;
        this.ruleTranslator = ruleTranslator;
        this.translator = translator;
        this.ruleService = ruleService;
        this.surfaceService = surfaceService;
        this.featureTranslator = featureTranslator;
    }


    /**
     * Get exceptions by case
     */
    private final Route fetchExceptions = doInTransaction(false, (request, response) -> {

        Analysis analysis = this.analysisService.get(this.getParamAnalysisId(request));

        Set<AnalysisException> exceptions = analysis.getAnalysisCase().getExceptions();

        if(request.queryMap().hasKey("type")){
            Integer type = request.queryMap().get("type").integerValue();

            return exceptions
                    .stream()
                    .filter(e -> e.getType().ordinal() == type)
                    .map(e -> translator.getAsDTO(e))
                    .collect(Collectors.toList());
        }

        return exceptions
                .stream()
                .map(e -> translator.getAsDTO(e))
                .collect(Collectors.toList());
    });

    /**
     * Create a rule exception for an analysis case
     */
    private final Route createRuleException = doInTransaction(true, (request, response) -> {

        AnalysisExceptionRuleDTO dto = ruleTranslator.getAsDTO(request.body());

        AnalysisExceptionRule domain = ruleTranslator.getAsDomain(dto);

        ruleService.create(domain);

        return ruleTranslator.getAsDTO(domain);
    });

    /**
     * Create a surface exception for an analysis case
     */
    private final Route createSurfaceException = doInTransaction(true, (request, response) -> {

        AnalysisExceptionSurfaceDTO dto = surfaceTranslator.getAsDTO(request.body());

        AnalysisExceptionSurface domain = surfaceTranslator.getAsDomain(dto);

        surfaceService.create(domain);

        return surfaceTranslator.getAsDTO(domain);
    });

    /**
     * Get a rule exception for an analysis case
     */
    private final Route fetchRuleException = doInTransaction(false, (request, response) -> {
        AnalysisExceptionRule exception = this.ruleService.get(this.getParamExceptionId(request));

        return ruleTranslator.getAsDTO(exception);
    });

    /**
     * Get a surface exception for an analysis case
     */
    private final Route fetchSurfaceException = doInTransaction(false, (request, response) -> {
        AnalysisExceptionSurface exception = this.surfaceService.get(this.getParamExceptionId(request));

        return surfaceTranslator.getAsDTO(exception);
    });

    private final Route fetchExceptionFeature = doInTransaction(false, (request, response) -> {
        AnalysisExceptionSurface exception = this.surfaceService.get(this.getParamExceptionId(request));

        return featureTranslator.getAsDTO(this.surfaceService.getFeature(exception));
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


            post("/rule", createRuleException, jsonTransformer);
            post("/surface", createSurfaceException, jsonTransformer);
            //post("/dynamicSurface", createDynamicSurfaceException, jsonTransformer);

            get("/rule/:" + EXCEPTION_ID_PARAM, fetchRuleException, jsonTransformer);

            get("/surface/:" + EXCEPTION_ID_PARAM, fetchSurfaceException, jsonTransformer);
            get("/surface/:" + EXCEPTION_ID_PARAM + "/feature", fetchExceptionFeature, jsonTransformer);

            //get("/dynamicSurface/:" + EXCEPTION_ID_PARAM, fetchException, jsonTransformer);

            put("/rule/:" + EXCEPTION_ID_PARAM, updateException, jsonTransformer);
            put("/surface/:" + EXCEPTION_ID_PARAM, updateException, jsonTransformer);
            //put("/dynamicSurface/:" + EXCEPTION_ID_PARAM, updateException, jsonTransformer);

            delete("/:" + EXCEPTION_ID_PARAM, deleteException, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/:" + ANALYSIS_ID_PARAM + "/case/exceptions" ;
    }
}
