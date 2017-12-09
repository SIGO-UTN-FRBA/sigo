package ar.edu.utn.frba.proyecto.sigo.router.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.bmvbw.RegulationBMVBW;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.easa.RegulationEASA;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.RegulationFAA13A;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.RegulationFAA18B;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.RegulationFAAPart77;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.RegulationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.tcanada.RegulationTpCanada312_4;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.tcanada.RegulationTpCanada312_5;
import ar.edu.utn.frba.proyecto.sigo.dto.common.EnumerationDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class RegulationRouter extends SigoRouter {

    JsonTransformer jsonTransformer;

    @Inject
    public RegulationRouter(
            JsonTransformer jsonTransformer
    ){
        super(null, null);

        this.jsonTransformer = jsonTransformer;
    }

    private final Route fetchRegulations = (request, response) -> {
        return Arrays.stream(Regulations.values())
                .map(r -> new EnumerationDTO(r.ordinal(), r.name(), r.description()))
                .collect(Collectors.toList());
    };

    private final Route fetchRegulation = (request, response) -> {
        return Regulation.of(Regulations.values()[Math.toIntExact(getParamRegulationId(request))]);
    };

    @Override
    public RouteGroup routes() {
        return ()-> {
            get("", fetchRegulations, jsonTransformer);
            get("/:" + REGULATION_ID_PARAM, fetchRegulation, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/regulations";
    }
}
