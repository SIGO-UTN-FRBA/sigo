package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.stream.Collectors;

import static spark.Spark.get;

public class AnalysisCaseRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisCaseService caseService;
    private AnalysisCaseTranslator translator;

    @Inject
    public AnalysisCaseRouter(
            HibernateUtil hibernateUtil,
            JsonTransformer jsonTransformer,
            AnalysisCaseService caseService,
            AnalysisCaseTranslator translator
    ){
        this.jsonTransformer = jsonTransformer;
        this.caseService = caseService;
        this.translator = translator;
        this.hibernateUtil = hibernateUtil;
    }

    private final Route searchCases = doInTransaction(false, (request, response) -> {
        return this.caseService.find(request.queryMap())
            .stream()
            .map(c -> translator.getAsDTO(c))
            .collect(Collectors.toList());
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            get("",searchCases, jsonTransformer);
            //get("/on-going", searchOnGoingCases, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/cases";
    }
}
