package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisObjectTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.stream.Collectors;

import static java.lang.String.format;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class AnalysisCaseRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisCaseService caseService;
    private AnalysisCaseTranslator caseTranslator;
    private AnalysisObjectTranslator objectTranslator;

    @Inject
    public AnalysisCaseRouter(
            HibernateUtil hibernateUtil,
            Gson gson,
            JsonTransformer jsonTransformer,
            AnalysisCaseService caseService,
            AnalysisCaseTranslator caseTranslator,
            AnalysisObjectTranslator objectTranslator
    ){
        this.objectMapper = gson;
        this.jsonTransformer = jsonTransformer;
        this.caseService = caseService;
        this.caseTranslator = caseTranslator;
        this.objectTranslator = objectTranslator;
        this.hibernateUtil = hibernateUtil;
    }


    /**
     * Get calculated objects (static)
     */
    private final Route fetchAnalysisObject = doInTransaction(true, (request, response) -> {

        AnalysisCase analysisCase = this.caseService.get(this.getParamCaseId(request));

        return analysisCase.getObjects()
                .stream()
                .map(o -> objectTranslator.getAsDTO(o))
                .collect(Collectors.toList());
    });

    /**
     * Calculate related objects to the case depending on their distance to the airport
     */
    private final Route updateAnalysisObject = doInTransaction(true, (request, response) -> {

        AnalysisCase analysisCase = this.caseService.get(this.getParamCaseId(request));

        caseService.updateObjects(analysisCase);

        return analysisCase.getObjects()
                .stream()
                .map(o -> objectTranslator.getAsDTO(o))
                .collect(Collectors.toList());
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            get("/objects", fetchAnalysisObject, jsonTransformer);
            put("/objects", updateAnalysisObject, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/:" + ANALYSIS_ID_PARAM + "/case";
    }
}
