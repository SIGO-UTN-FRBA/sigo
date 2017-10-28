package ar.edu.utn.frba.proyecto.sigo.router.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.bmvbw.RegulationBMVBW;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.easa.RegulationEASA;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.RegulationFAA;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.RegulationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.tcanada.RegulationTCanada312_5;
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
        this.jsonTransformer = jsonTransformer;
    }

    private final Route fetchRegulations = (request, response) -> {
        return Arrays.stream(Regulations.values())
                .map(r -> new EnumerationDTO(r.ordinal(), r.name(), r.description()))
                .collect(Collectors.toList());
    };

    private final Route fetchRegulation = (request, response) -> {
        switch (Regulations.values()[Math.toIntExact(getParamRegulationId(request))]) {

            case ICAO_ANNEX_14:
                return new RegulationICAOAnnex14();
            case EASA:
                return new RegulationEASA();
            case BMVBW:
                return new RegulationBMVBW();
            case TRANSPORT_CANADA_5TH:
                return new RegulationTCanada312_5();
            case FAA_CFR_PART_77:
                return new RegulationFAA();
            default:
                throw new InvalidParameterException("regulation_id");
        }
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
