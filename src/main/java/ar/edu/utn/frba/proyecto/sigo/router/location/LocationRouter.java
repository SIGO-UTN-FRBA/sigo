package ar.edu.utn.frba.proyecto.sigo.router.location;

import ar.edu.utn.frba.proyecto.sigo.dto.common.ListItemDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.service.location.PoliticalLocationService;
import ar.edu.utn.frba.proyecto.sigo.spark.JsonTransformer;
import spark.Route;
import spark.RouteGroup;

import javax.inject.Inject;

import java.util.stream.Collectors;

import static spark.Spark.get;

public class LocationRouter extends SigoRouter {

    JsonTransformer jsonTransformer;
    PoliticalLocationService locationService;

    @Inject
    public LocationRouter(
        HibernateUtil hibernateUtil,
        JsonTransformer jsonTransformer,
        PoliticalLocationService locationService
    ){
        this.hibernateUtil = hibernateUtil;
        this.jsonTransformer = jsonTransformer;
        this.locationService = locationService;
    }

    private final Route fetchLocations = doInTransaction(false, (request, response) -> {

        if(!request.queryMap().hasKey("type"))
            throw new MissingParameterException("Missing query parameter: 'type'. It is a name of location type.");

        return locationService.find(request.queryMap())
                .stream()
                .map(l -> new ListItemDTO(l.getId(), l.getName())) //TODO fullpath as value
                .collect(Collectors.toList());
    });

    @Override
    public RouteGroup routes() {
        return ()-> {
            get("", fetchLocations, jsonTransformer);
        };
    }

    @Override
    public String path() {
        return "/locations";
    }
}
