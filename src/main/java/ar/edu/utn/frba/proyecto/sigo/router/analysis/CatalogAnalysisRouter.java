package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisResultReasonService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import org.hibernate.SessionFactory;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import static spark.Spark.get;

public class CatalogAnalysisRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisResultReasonService resultReasonService;

    @Inject
    public CatalogAnalysisRouter(
            Gson objectMapper,
            SessionFactory sessionFactory,
            JsonTransformer jsonTransformer,
            AnalysisResultReasonService resultReasonService
    ) {

        super(objectMapper, sessionFactory);

        this.jsonTransformer = jsonTransformer;
        this.resultReasonService = resultReasonService;
    }

    private final Route fetchAnalysisResultReasons = doInTransaction(
            false,
            (request, response) -> resultReasonService.findAll()
    );

    @Override
    public RouteGroup routes() {
        return ()->{
            get("/resultReasons",fetchAnalysisResultReasons, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/catalogs/analysis";
    }
}
