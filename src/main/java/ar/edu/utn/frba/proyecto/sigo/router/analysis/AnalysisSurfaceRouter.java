package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.translator.SimpleFeatureTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisSurfaceService;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisSurfaceTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import spark.Route;
import spark.RouteGroup;
import static spark.Spark.get;

import javax.inject.Inject;
import java.util.stream.Collectors;

public class AnalysisSurfaceRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisSurfaceService surfaceService;
    private AnalysisSurfaceTranslator surfaceTranslator;
    private SimpleFeatureTranslator featureTranslator;

    @Inject
    public AnalysisSurfaceRouter(
            Gson objectMapper,
            HibernateUtil hibernateUtil,
            JsonTransformer jsonTransformer,
            AnalysisSurfaceService surfaceService,
            AnalysisSurfaceTranslator surfaceTranslator,
            SimpleFeatureTranslator featureTranslator
    ) {
        super(objectMapper, hibernateUtil);

        this.jsonTransformer = jsonTransformer;
        this.surfaceService = surfaceService;
        this.surfaceTranslator = surfaceTranslator;
        this.featureTranslator = featureTranslator;
    }

    /**
     * Get all surfaces as features for a given direction
     */
    private final Route fetchSurfacesAsFeatures = doInTransaction(false, (request, response) -> {

        return this.surfaceService.find(getParamAnalysisId(request), request.queryMap())
                .map( s -> featureTranslator.getAsDTO(this.surfaceService.getFeature(s)))
                .collect(Collectors.toList());
    });

    private final Route fetchSurface = doInTransaction(false, (request, response) -> {

        AnalysisSurface domain = this.surfaceService.get(getParamSurfaceId(request));

        return surfaceTranslator.getAsDTO(domain);
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            get("", fetchSurfacesAsFeatures, jsonTransformer);
            get("/:" + SURFACE_ID_PARAM, fetchSurface, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/:" + ANALYSIS_ID_PARAM + "/case/surfaces" ;
    }
}
