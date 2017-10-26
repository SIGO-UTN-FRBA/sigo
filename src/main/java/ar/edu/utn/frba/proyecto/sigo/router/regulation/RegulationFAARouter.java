package ar.edu.utn.frba.proyecto.sigo.router.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAAAircraftApproachCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAAAircraftClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAAAircraftDesignGroups;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAARunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAARunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAARunwaysTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.RegulationFAA;
import ar.edu.utn.frba.proyecto.sigo.dto.common.EnumerationDTO;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.stream.Collectors;

import static spark.Spark.get;

public class RegulationFAARouter extends SigoRouter {

    private JsonTransformer jsonTransformer;

    @Inject
    public RegulationFAARouter(
            HibernateUtil hibernateUtil,
            JsonTransformer jsonTransformer
    ){

        this.jsonTransformer = jsonTransformer;
        this.hibernateUtil = hibernateUtil;
    }

    private final Route fetchRunwayCategories = (request, response) -> {
        return Arrays.stream(FAARunwayCategories.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.code()))
                .collect(Collectors.toList());
    };

    private final Route fetchRunwayClassifications = (request, response) -> {
        return Arrays.stream(FAARunwayClassifications.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.code()))
                .collect(Collectors.toList());
    };

    private final Route fetchRunwaysTypes = (request, response) -> {
        return Arrays.stream(FAARunwaysTypes.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.name()))
                .collect(Collectors.toList());
    };

    private final Route fetchAircraftApproachCategories = (request, response) -> {
        return Arrays.stream(FAAAircraftApproachCategories.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.name()))
                .collect(Collectors.toList());
    };

    private final Route fetchAircraftClassifications = (request, response) -> {
        return Arrays.stream(FAAAircraftClassifications.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.name()))
                .collect(Collectors.toList());
    };

    private final Route fetchAircraftDesignGroups = (request, response) -> {
        return Arrays.stream(FAAAircraftDesignGroups.values())
                .map(o -> new EnumerationDTO(o.ordinal(), o.name(), o.name()))
                .collect(Collectors.toList());
    };

    @Override
    public RouteGroup routes() {
        return () -> {
            get("/runwayCategories", fetchRunwayCategories, jsonTransformer);
            get("/runwayClassifications", fetchRunwayClassifications, jsonTransformer);
            get("/runwaysTypes", fetchRunwaysTypes, jsonTransformer);
            get("/aircraftApproachCategories", fetchAircraftApproachCategories, jsonTransformer);
            get("/aircraftClassifications", fetchAircraftClassifications, jsonTransformer);
            get("/aircraftDesignGroups", fetchAircraftDesignGroups, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/regulations/"+ Regulations.FAA_CFR_PART_77.ordinal();
    }
}
