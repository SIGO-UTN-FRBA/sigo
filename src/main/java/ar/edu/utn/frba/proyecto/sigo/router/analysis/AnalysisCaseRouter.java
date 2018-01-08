package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseService;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisCaseTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisObjectTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static java.lang.String.format;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.put;

public class AnalysisCaseRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisCaseService caseService;
    private AnalysisService analysisService;
    private AnalysisCaseTranslator caseTranslator;
    private AnalysisObjectTranslator objectTranslator;

    @Inject
    public AnalysisCaseRouter(
            HibernateUtil hibernateUtil,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            AnalysisCaseService caseService,
            AnalysisCaseTranslator caseTranslator,
            AnalysisObjectTranslator objectTranslator,
            AnalysisService analysisService
    ){
        super(objectMapper, hibernateUtil);

        this.jsonTransformer = jsonTransformer;
        this.caseService = caseService;
        this.caseTranslator = caseTranslator;
        this.objectTranslator = objectTranslator;
        this.analysisService = analysisService;
    }

    /**
     * Get analysis case instance by identifier
     */
    private final Route fetchAnalysisCase = doInTransaction(false, (request, response) -> {

        AnalysisCase analysisCase = this.caseService.get(this.getParamAnalysisId(request));

        return caseTranslator.getAsDTO(analysisCase);
    });

    /**
     * Calculate related objects to the case depending on their distance to the airport
     */
    private final Route updateAnalysisCase = doInTransaction(true, (request, response) -> {

        Analysis analysis = this.analysisService.get(this.getParamAnalysisId(request));

        JsonObject jsonObject = objectMapper.fromJson(request.body(), JsonObject.class);

        if(!jsonObject.has("searchRadius"))
            throw new MissingParameterException("searchRadius");

        Double radius = jsonObject.get("searchRadius").getAsDouble();

        AnalysisCase analysisCase = analysis.getAnalysisCase();

        caseService.updateObjects(analysisCase, radius);

        return caseTranslator.getAsDTO(analysisCase);
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            get("", fetchAnalysisCase, jsonTransformer);
            patch("", updateAnalysisCase, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/:" + ANALYSIS_ID_PARAM + "/case";
    }
}
