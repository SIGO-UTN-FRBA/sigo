package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisAdverseEffectAspectService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisAdverseEffectAspectTranslator;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.AnalysisAdverseEffectMitigationTranslator;
import com.google.gson.Gson;
import org.hibernate.SessionFactory;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class CatalogAnalysisRouter extends SigoRouter {

    private JsonTransformer jsonTransformer;
    private AnalysisAdverseEffectAspectService aspectService;
    private AnalysisAdverseEffectMitigationTranslator mitigationTranslator;
    private AnalysisAdverseEffectAspectTranslator aspectTranslator;

    @Inject
    public CatalogAnalysisRouter(
            Gson objectMapper,
            SessionFactory sessionFactory,
            JsonTransformer jsonTransformer,
            AnalysisAdverseEffectAspectService aspectService,
            AnalysisAdverseEffectMitigationTranslator mitigationTranslator,
            AnalysisAdverseEffectAspectTranslator aspectTranslator
    ) {

        super(objectMapper, sessionFactory);

        this.jsonTransformer = jsonTransformer;
        this.aspectService = aspectService;
        this.mitigationTranslator = mitigationTranslator;
        this.aspectTranslator = aspectTranslator;
    }

    private final Route fetchAdverseEffectAspects = doInTransaction(
            false,
            (request, response) -> aspectService.findAll().stream().map(a -> aspectTranslator.getAsDTO(a)).collect(Collectors.toList())
    );

    private final Route fetchAdverseEffectMitigations = doInTransaction(
            false,
            (request, response) -> aspectService.get(getParamAspectId(request)).getMitigations().stream().map( m -> mitigationTranslator.getAsDTO(m)).collect(Collectors.toList())
    );

    @Override
    public RouteGroup routes() {
        return ()->{
            get("/aspects", fetchAdverseEffectAspects, jsonTransformer);
            get("/aspects/:" + ASPECT_ID_PARAM + "/mitigations", fetchAdverseEffectMitigations, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/catalogs/analysis";
    }
}
