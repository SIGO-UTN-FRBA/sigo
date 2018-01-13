package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisResult;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisObstacleService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisObstacleTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisResultTranslator;
import com.google.gson.Gson;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class AnalysisObstacleRouter extends SigoRouter{

    private JsonTransformer jsonTransformer;
    private AnalysisObstacleService obstacleService;
    private AnalysisObstacleTranslator translator;
    private AnalysisResultTranslator resultTranslator;

    @Inject
    public AnalysisObstacleRouter(
            Gson objectMapper,
            HibernateUtil hibernateUtil,
            JsonTransformer jsonTransformer,
            AnalysisObstacleService obstacleService,
            AnalysisObstacleTranslator obstacleTranslator,
            AnalysisResultTranslator resultTranslator
    ) {
        super(objectMapper, hibernateUtil);

        this.jsonTransformer = jsonTransformer;
        this.obstacleService = obstacleService;
        this.translator = obstacleTranslator;
        this.resultTranslator = resultTranslator;
    }

    private final Route fetchObstacles = doInTransaction(false , (request, response) -> {
        return this.obstacleService.find(getParamAnalysisId(request))
                        .map(translator::getAsDTO)
                        .collect(Collectors.toList());
    });

    private final Route fetchResult = doInTransaction(false, (request, response) -> {
        AnalysisResult domain = this.obstacleService.get(getParamObstacleId(request)).getResult();

        return this.resultTranslator.getAsDTO(domain);
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            get("", fetchObstacles, jsonTransformer);
            get("/:" + OBSTACLE_ID_PARAM + "/result", fetchResult, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/:" + ANALYSIS_ID_PARAM + "/case/obstacles";
    }
}
