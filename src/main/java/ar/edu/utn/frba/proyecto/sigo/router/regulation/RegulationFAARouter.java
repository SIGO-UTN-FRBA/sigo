package ar.edu.utn.frba.proyecto.sigo.router.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.*;
import ar.edu.utn.frba.proyecto.sigo.dto.common.EnumerationDTO;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import org.hibernate.SessionFactory;
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
            SessionFactory sessionFactory,
            JsonTransformer jsonTransformer
    ){
        super(null, sessionFactory);

        this.jsonTransformer = jsonTransformer;
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
        return "/regulations/faa77";
    }
}
