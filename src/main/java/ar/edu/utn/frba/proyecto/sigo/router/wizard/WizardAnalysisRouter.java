package ar.edu.utn.frba.proyecto.sigo.router.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisService;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.service.wizard.WizardAnalysis;
import com.google.gson.Gson;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static spark.Spark.post;

public class WizardAnalysisRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisService analysisService;
    private AnalysisTranslator analysisTranslator;
    private WizardAnalysis wizard;

    @Inject
    public WizardAnalysisRouter(
            HibernateUtil hibernateUtil,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            WizardAnalysis wizard,
            AnalysisService analysisService,
            AnalysisTranslator analysisTranslator
    ){
        super(objectMapper, hibernateUtil);

        this.jsonTransformer = jsonTransformer;
        this.wizard = wizard;
        this.analysisService = analysisService;
        this.analysisTranslator = analysisTranslator;
    }

    private final Route updateAnalysis = doInTransaction(true, (request, response) -> {

        if(!request.queryMap().hasKey("action"))
            throw new MissingParameterException("action");

        Analysis analysis = this.analysisService.get(getParamAnalysisId(request));

        switch (request.queryMap().get("action").value()){
            case "next":
                wizard.goNext(analysis);
                break;
            case "previous":
                wizard.goPrevious(analysis);
                break;
            case "finish":
                wizard.finish(analysis);
                break;
            case "cancel":
                wizard.cancel(analysis);
                break;

            //TODO case "initialize"

            default:
                throw new InvalidParameterException("Action does not exists");
        }

        return analysisTranslator.getAsDTO(analysis);
    });


    @Override
    public RouteGroup routes() {
        return ()->{
            post("/:" + ANALYSIS_ID_PARAM, updateAnalysis, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/wizard";
    }
}
