package ar.edu.utn.frba.proyecto.sigo.router.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisObjectDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisObjectService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisObjectTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.patch;

public class AnalysisObjectRouter extends SigoRouter {

    private AnalysisService analysisService;
    private JsonTransformer jsonTransformer;
    private AnalysisObjectTranslator objectTranslator;
    private AnalysisObjectService objectService;

    @Inject
    public AnalysisObjectRouter(
            Gson objectMapper,
            HibernateUtil hibernateUtil,
            AnalysisService analysisService,
            JsonTransformer jsonTransformer,
            AnalysisObjectTranslator objectTranslator,
            AnalysisObjectService objectService
    ) {
        super(objectMapper, hibernateUtil);
        this.analysisService = analysisService;
        this.jsonTransformer = jsonTransformer;
        this.objectTranslator = objectTranslator;
        this.objectService = objectService;
    }

    /**
     * Get calculated objects (static)
     */
    private final Route fetchObjects = doInTransaction(true, (request, response) -> {

        Analysis analysis = this.analysisService.get(this.getParamAnalysisId(request));

        return analysis.getAnalysisCase().getObjects()
                .stream()
                .map(o -> objectTranslator.getAsDTO(o))
                .collect(Collectors.toList());
    });

    /**
     * Set analysis object as included/excluded given its identifier
     */
    private final Route includeObject = doInTransaction(true, (request, response) -> {

        AnalysisObject domain = objectService.get(getParamObjectId(request));

        JsonObject jsonObject = objectMapper.fromJson(request.body(), JsonObject.class);

        if(!jsonObject.has("included"))
            throw new MissingParameterException("included");

        domain.setIncluded(jsonObject.get("included").getAsBoolean());

        return objectTranslator.getAsDTO(domain);
    });

    @Override
    public RouteGroup routes() {

        //TODO validar que se encuentre en stage correcto

        return ()->{
            get("", fetchObjects, jsonTransformer);
            patch("/:" + OBJECT_ID_PARAM, includeObject, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/analysis/:" + ANALYSIS_ID_PARAM + "/case/objects" ;
    }
}
