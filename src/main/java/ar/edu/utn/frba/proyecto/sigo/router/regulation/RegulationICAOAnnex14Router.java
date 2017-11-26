package ar.edu.utn.frba.proyecto.sigo.router.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeLetters;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14Surface;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14Surfaces;
import ar.edu.utn.frba.proyecto.sigo.dto.common.EnumerationDTO;
import ar.edu.utn.frba.proyecto.sigo.dto.common.ListItemDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Translator;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import spark.QueryParamsMap;
import spark.Request;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class RegulationICAOAnnex14Router extends SigoRouter {

    private final JsonTransformer jsonTransformer;
    private OlsRuleService ruleService;
    private OlsRuleICAOAnnex14Translator icaoAnnex14Translator;

    @Inject
    public RegulationICAOAnnex14Router(
            JsonTransformer jsonTransformer,
            HibernateUtil hibernateUtil,
            OlsRuleService ruleService,
            OlsRuleICAOAnnex14Translator icaoAnnex14Translator,
            Gson gson
    ){
        super(gson, hibernateUtil);

        this.ruleService = ruleService;
        this.icaoAnnex14Translator = icaoAnnex14Translator;
        this.hibernateUtil = hibernateUtil;
        this.jsonTransformer = jsonTransformer;
    }

    private ICAOAnnex14RunwayCodeNumbers getParamNumberCode(Request request){
        return Optional.ofNullable(request.queryMap("number"))
                .map(QueryParamsMap::integerValue)
                .map(c -> {

                    if(c >= ICAOAnnex14RunwayCodeNumbers.values().length)
                        throw new InvalidParameterException(String.format("Code number '%s' does not exists.", c));

                    return ICAOAnnex14RunwayCodeNumbers.values()[c];
                })
                .orElseThrow(() -> new MissingParameterException("number"));
    }

    private ICAOAnnex14RunwayClassifications getParamClassification(Request request){
        return Optional.ofNullable(request.queryMap("classification"))
                .map(QueryParamsMap::integerValue)
                .map( c -> {

                    if(c >= ICAOAnnex14RunwayClassifications.values().length)
                        throw new InvalidParameterException(String.format("Classification '%s' does not exists.", c));

                    return ICAOAnnex14RunwayClassifications.values()[c];
                })
                .orElseThrow(()-> new MissingParameterException("classification"));
    }

    private ICAOAnnex14RunwayCategories getParamCategory(Request request){
        return Optional.ofNullable(request.queryMap("category"))
                .map(QueryParamsMap::integerValue)
                .map(c -> {

                    if(c >= ICAOAnnex14RunwayCategories.values().length)
                        throw new InvalidParameterException(String.format("Category '%s' does not exists.", c));

                    return ICAOAnnex14RunwayCategories.values()[c];
                })
                .orElseThrow(() -> new MissingParameterException("category"));
    }

    private Boolean getParamRecommendations(Request request) {
        return Optional.ofNullable(request.queryMap("recommendations"))
                .map(QueryParamsMap::booleanValue)
                .orElseThrow(() -> new MissingParameterException("recommendations"));
    }

    private ICAOAnnex14Surfaces getParamSurface(Request request) {
        return Optional.ofNullable(this.getParamSurfaceId(request))
                .map(c -> {

                    if(c >= ICAOAnnex14Surfaces.values().length)
                        throw new InvalidParameterException(String.format("Surface '%s' does not exists.", c));

                    return ICAOAnnex14Surfaces.values()[Math.toIntExact(c)];
                }).get();
    }

    private final Route fetchRunwayCategories = (request, response) -> {
        return Arrays.stream(ICAOAnnex14RunwayCategories.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.code()))
                .collect(Collectors.toList());
    };

    private final Route fetchRunwayClassifications = (request, response) -> {
        return Arrays.stream(ICAOAnnex14RunwayClassifications.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.description()))
                .collect(Collectors.toList());
    };

    private final Route fetchRunwayCodeLetters = (request, response) -> {
        return Arrays.stream(ICAOAnnex14RunwayCodeLetters.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.name()))
                .collect(Collectors.toList());
    };

    private final Route fetchRunwayCodeNumbers = (request, response) -> {
        return Arrays.stream(ICAOAnnex14RunwayCodeNumbers.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.description()))
                .collect(Collectors.toList());
    };

    private final Route fetchSurfaces = doInTransaction(false, (request, response) -> {

        return ruleService.getICAOAnnex14Surfaces(
                this.getParamClassification(request),
                this.getParamCategory(request),
                this.getParamNumberCode(request),
                this.getParamRecommendations(request)
        )
                .stream()
                .sorted(Comparator.comparingInt(Enum::ordinal))
                .map(s -> new ListItemDTO((long) s.ordinal(), s.description()))
                .collect(Collectors.toList());
    });

    private final Route fetchSurface = doInTransaction(false, (request, response) -> {

        ICAOAnnex14Surface surface = null;

        ICAOAnnex14RunwayCodeNumbers paramNumberCode = this.getParamNumberCode(request);
        ICAOAnnex14RunwayClassifications paramClassification = this.getParamClassification(request);
        ICAOAnnex14RunwayCategories paramCategory = this.getParamCategory(request);

        switch (this.getParamSurface(request)){

            case STRIP:
                surface = ruleService.getICAOAnnex14SurfaceStrip(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case CONICAL:
                surface = ruleService.getICAOAnnex14SurfaceConical(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case INNER_HORIZONTAL:
                surface = ruleService.getICAOAnnex14SurfaceInnerHorizontal(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case INNER_APPROACH:
                surface = ruleService.getICAOAnnex14SurfaceInnerApproach(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case APPROACH:
                surface = ruleService.getICAOAnnex14SurfaceApproach(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case APPROACH_FIRST_SECTION:
                surface = ruleService.getICAOAnnex14SurfaceApproachFirstSection(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case APPROACH_SECOND_SECTION:
                surface = ruleService.getICAOAnnex14SurfaceApproachSecondSection(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case APPROACH_HORIZONTAL_SECTION:
                surface = ruleService.getICAOAnnex14SurfaceApproachHorizontalSection(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case TRANSITIONAL:
                surface = ruleService.getICAOAnnex14SurfaceTransitional(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case INNER_TRANSITIONAL:
                surface = ruleService.getICAOAnnex14SurfaceInnerTransitional(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case BALKED_LANDING_SURFACE:
                surface = ruleService.getICAOAnnex14SurfaceBalkedLanding(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
            case TAKEOFF_CLIMB:
                surface = ruleService.getICAOAnnex14SurfaceTakeoffClimb(
                        paramNumberCode,
                        paramClassification,
                        paramCategory
                );
                break;
        }

        surface.setCategory(paramCategory);
        surface.setClassification(paramClassification);
        surface.setCode(paramNumberCode);

        //TODO llevar a translator

        JsonObject jsonObject = objectMapper.toJsonTree(surface).getAsJsonObject();

        jsonObject.addProperty("name", surface.getName());

        jsonObject.addProperty("id", surface.getId());

        return jsonObject;
    });

    private final Route fetchRules = doInTransaction(false, (request, response) -> {



        return this.ruleService.getICAOAnnex14Rules()
                .stream()
                .map(r -> this.icaoAnnex14Translator.getAsDTO(r.getIcaoRule()))
                .collect(Collectors.toList());
    });

    @Override
    public RouteGroup routes() {
        return ()-> {

            get("/rules", fetchRules, jsonTransformer);
            get("/runwayCategories", fetchRunwayCategories, jsonTransformer);
            get("/runwayClassifications", fetchRunwayClassifications, jsonTransformer);
            get("/runwayCodeLetters", fetchRunwayCodeLetters, jsonTransformer);
            get("/runwayCodeNumbers", fetchRunwayCodeNumbers, jsonTransformer);

            get("/surfaces", fetchSurfaces ,jsonTransformer);
            get("/surfaces/:" + SURFACE_ID_PARAM, fetchSurface, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/regulations/icao14";
    }
}
