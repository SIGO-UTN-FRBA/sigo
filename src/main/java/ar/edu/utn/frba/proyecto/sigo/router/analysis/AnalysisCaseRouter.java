package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisObjectService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisObjectTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.stream.Collectors;

import static java.lang.String.format;
import static spark.Spark.get;
import static spark.Spark.post;

public class AnalysisCaseRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisCaseService caseService;
    private AnalysisCaseTranslator caseTranslator;
    private AnalysisObjectTranslator objectTranslator;
    private AnalysisObjectService objectService;

    @Inject
    public AnalysisCaseRouter(
            HibernateUtil hibernateUtil,
            JsonTransformer jsonTransformer,
            AnalysisCaseService caseService,
            AnalysisCaseTranslator caseTranslator,
            AnalysisObjectTranslator objectTranslator,
            AnalysisObjectService objectService
    ){
        this.jsonTransformer = jsonTransformer;
        this.caseService = caseService;
        this.caseTranslator = caseTranslator;
        this.objectTranslator = objectTranslator;
        this.objectService = objectService;
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
    private final Route determineAnalysisObject = doInTransaction(true, (request, response) -> {

        AnalysisCase analysisCase = this.caseService.get(this.getParamCaseId(request));

        objectService.updateAnalysedObjects(analysisCase);

        return analysisCase.getObjects()
                .stream()
                .map(o -> objectTranslator.getAsDTO(o))
                .collect(Collectors.toList());
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            get("",searchCases, jsonTransformer);
            //TODO get("/on-going", searchOnGoingCases, jsonTransformer);

            get(format("/:%s/objects", CASE_ID_PARAM), fetchAnalysisObject, jsonTransformer);
            post(format("/:%s/objects", CASE_ID_PARAM), determineAnalysisObject, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/cases";
    }
}
