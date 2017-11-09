package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.post;

public class AnalysisRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisService analysisService;
    private AnalysisTranslator analysisTranslator;

    @Inject
    public AnalysisRouter(
            HibernateUtil hibernateUtil,
            Gson gson,
            JsonTransformer jsonTransformer,
            AnalysisService analysisService,
            AnalysisTranslator analysisTranslator
    ){
        this.objectMapper = gson;
        this.jsonTransformer = jsonTransformer;
        this.analysisService = analysisService;
        this.analysisTranslator = analysisTranslator;
        this.hibernateUtil = hibernateUtil;
    }

    /**
     * Search analysis instances filtered by parameters
     */
    private final Route searchAnalysis = doInTransaction(false, (request, response) -> {
        return this.analysisService.find(request.queryMap())
                .stream()
                .map(c -> analysisTranslator.getAsDTO(c))
                .collect(Collectors.toList());
    });

    /**
     * Create a case depending on older case.
     */
    private final Route createAnalysis = doInTransaction(true, (request, response) -> {

        JsonObject jsonObject = objectMapper.fromJson(request.body(), JsonObject.class);

        if(!jsonObject.has("parentId"))
            throw new MissingParameterException("parentId");

        Analysis baseCase = this.analysisService.get(jsonObject.get("parentId").getAsLong());

        Analysis analysisCase = this.analysisService.create(new Analysis(), baseCase);

        return analysisTranslator.getAsDTO(analysisCase);
    });


    /**
     * Get an analysis instance by its identifier
     */
    private final Route fetchAnalysis = doInTransaction(false, (request, response) -> {

        Analysis analysis = this.analysisService.get(getParamAnalysisId(request));

        return analysisTranslator.getAsDTO(analysis);
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            post("", createAnalysis, jsonTransformer);
            get("", searchAnalysis, jsonTransformer);

            get("/:" + ANALYSIS_ID_PARAM, fetchAnalysis, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis";
    }
}