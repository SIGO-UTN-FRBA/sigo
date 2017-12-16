package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisSurfaceService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import spark.Route;
import spark.RouteGroup;
import static spark.Spark.get;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class AnalysisSurfaceRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisSurfaceService surfaceService;

    @Inject
    public AnalysisSurfaceRouter(
            Gson objectMapper,
            HibernateUtil hibernateUtil,
            JsonTransformer jsonTransformer,
            AnalysisSurfaceService surfaceService
    ) {
        super(objectMapper, hibernateUtil);

        this.jsonTransformer = jsonTransformer;
        this.surfaceService = surfaceService;
    }

    /**
     * Get all surfaces as features for a given direction
     */
    private final Route fetchSurfaces = doInTransaction(false, (request, response) -> {

        return this.surfaceService.find(request.queryMap())
                .map( s -> this.surfaceService.getFeature(s))
                .collect(Collectors.toList());
    });

    @Override
    public RouteGroup routes() {
        return ()->{
            get("", fetchSurfaces, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/:" + ANALYSIS_ID_PARAM + "/case/surfaces" ;
    }
}
