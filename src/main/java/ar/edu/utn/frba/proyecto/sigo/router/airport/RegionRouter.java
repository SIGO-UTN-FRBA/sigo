package ar.edu.utn.frba.proyecto.sigo.router.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RegionService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import ar.edu.utn.frba.proyecto.sigo.translator.location.RegionTranslator;
import com.google.gson.Gson;
import org.hibernate.SessionFactory;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class RegionRouter extends SigoRouter {

    private RegionService regionService;
    private JsonTransformer jsonTransformer;
    private RegionTranslator translator;

    @Inject
    public RegionRouter(
            SessionFactory sessionFactory,
            Gson objectMapper,
            JsonTransformer jsonTransformer,
            RegionService regionService,
            RegionTranslator translator
    ) {
        super(objectMapper, sessionFactory);

        this.jsonTransformer = jsonTransformer;
        this.regionService = regionService;
        this.translator = translator;
    }

    private final Route fetchRegions = doInTransaction(false,(request, response) -> {
        return regionService.findAll()
                .stream()
                .map(translator::getAsDTO)
                .collect(Collectors.toList());
    });

    private final Route fetchRegion = doInTransaction(false, (request, reponse) -> {
       Region region = regionService.get(getParamRegionId(request));

       return translator.getAsDTO(region);
    });


    @Override
    public RouteGroup routes() {
        return ()-> {
            get("", fetchRegions, jsonTransformer);

            get("/:" + REGION_ID_PARAM, fetchRegion, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/regions";
    }
}
