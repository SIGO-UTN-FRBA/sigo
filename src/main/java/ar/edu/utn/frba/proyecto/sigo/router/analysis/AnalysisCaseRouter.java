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

    private final Route searchCases = doInTransaction(false, (request, response) -> {
        return this.caseService.find(request.queryMap())
            .stream()
            .map(c -> caseTranslator.getAsDTO(c))
            .collect(Collectors.toList());
    });

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

    /**
     * Create a case depending on older case.
     */
    private final Route createCase = doInTransaction(true, (request, response) -> {

        JsonObject jsonObject = objectMapper.fromJson(request.body(), JsonObject.class);

        if(!jsonObject.has("baseId"))
            throw new MissingParameterException("baseId");

        AnalysisCase baseCase = this.caseService.get(jsonObject.get("baseId").getAsLong());

        AnalysisCase analysisCase = this.caseService.create(new AnalysisCase(), baseCase);

        return caseTranslator.getAsDTO(analysisCase);
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            post("", createCase, jsonTransformer);
            get("", searchCases, jsonTransformer);
            //TODO get("/on-going", searchOnGoingCases, jsonTransformer);

            get(format("/:%s/objects", CASE_ID_PARAM), fetchAnalysisObject, jsonTransformer);
            put(format("/:%s/objects", CASE_ID_PARAM), updateAnalysisObject, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/cases";
    }
}
