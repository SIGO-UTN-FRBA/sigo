package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisResult;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisResultDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.ResourceNotFoundException;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisObstacleService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisResultService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisObstacleTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisResultTranslator;
import com.google.gson.Gson;
import org.hibernate.SessionFactory;
import spark.QueryParamsMap;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.put;

public class AnalysisObstacleRouter extends SigoRouter{

    private JsonTransformer jsonTransformer;
    private AnalysisObstacleService obstacleService;
    private AnalysisObstacleTranslator translator;
    private AnalysisResultTranslator resultTranslator;
    private AnalysisResultService resultService;

    @Inject
    public AnalysisObstacleRouter(
            Gson objectMapper,
            SessionFactory sessionFactory,
            JsonTransformer jsonTransformer,
            AnalysisObstacleService obstacleService,
            AnalysisObstacleTranslator obstacleTranslator,
            AnalysisResultTranslator resultTranslator,
            AnalysisResultService resultService
    ) {

        super(objectMapper, sessionFactory);

        this.jsonTransformer = jsonTransformer;
        this.obstacleService = obstacleService;
        this.translator = obstacleTranslator;
        this.resultTranslator = resultTranslator;
        this.resultService = resultService;
    }

    private final Route fetchObstacles = doInTransaction(false , (request, response) -> {

        Optional<QueryParamsMap> exceptingParam = Optional.ofNullable(request.queryMap().get("excepting"));
        Optional<QueryParamsMap> validityParam = Optional.ofNullable(request.queryMap().get("validity"));

        return this.obstacleService.find(getParamAnalysisId(request))
                        .filter( o -> exceptingParam.map(param -> o.getIsExcepted() == param.booleanValue()).orElse(true))
                        .filter( o -> validityParam.map(param -> o.getIsValid() == param.booleanValue()).orElse(true))
                        .map(translator::getAsDTO)
                        .collect(Collectors.toList());
    });

    private final Route fetchObstacle = doInTransaction(false, (request, response) -> {
        Long obstacleId = getParamObstacleId(request);

        return Optional.ofNullable(this.obstacleService.get(obstacleId))
                    .map(translator::getAsDTO)
                    .orElseThrow(()-> new ResourceNotFoundException(String.format("obstacle_id = %s", obstacleId)));
    });

    private final Route fetchResult = doInTransaction(false, (request, response) -> {

        Long obstacleId = getParamObstacleId(request);

        return Optional.ofNullable(this.obstacleService.get(obstacleId).getResult())
            .map(domain -> this.resultTranslator.getAsDTO(domain))
            .orElseThrow(() -> new ResourceNotFoundException("result for analysis obstacle " + obstacleId));
    });

    private final Route saveResult = doInTransaction(true, (request, response) -> {

        Long obstacleId = getParamObstacleId(request);

        AnalysisObstacle analysisObstacle = this.obstacleService.get(obstacleId);

        AnalysisResultDTO dto = resultTranslator.getAsDTO(request.body());

        AnalysisResult analysisResult = resultTranslator.getAsDomain(dto);

        analysisResult = (Optional.ofNullable(analysisObstacle.getResult()).isPresent()) ?
            resultService.update(analysisResult)
        :
            resultService.create(analysisResult);

        return resultTranslator.getAsDTO(analysisResult);
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            get("", fetchObstacles, jsonTransformer);

            get("/:" + OBSTACLE_ID_PARAM, fetchObstacle, jsonTransformer);

            get("/:" + OBSTACLE_ID_PARAM + "/result", fetchResult, jsonTransformer);
            put("/:" + OBSTACLE_ID_PARAM + "/result", saveResult, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/:" + ANALYSIS_ID_PARAM + "/case/obstacles";
    }
}
