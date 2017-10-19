package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwaySurface;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RunwayTranslator extends Translator<Runway, RunwayDTO> {

    private AirportService airportService;
    private CatalogAirportService catalogService;

    @Inject
    public RunwayTranslator(
        Gson gson,
        AirportService airportService,
        CatalogAirportService catalogService
    ){
        this.airportService = airportService;
        this.catalogService = catalogService;
        this.objectMapper = gson;
        this.dtoClass = RunwayDTO.class;
        this.domainClass = Runway.class;
    }

    public RunwayDTO getAsDTO(Runway domain) {

        return RunwayDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .length(domain.getLength())
                .width(domain.getWidth())
                .surfaceId(domain.getSurface().getId())
                .airportId(domain.getAirport().getId())
                .build();
    }

    public Runway getAsDomain(RunwayDTO dto) {

        Runway.RunwayBuilder builder = Runway.builder();

        // basics attributes

        builder
                .id(dto.getId())
                .length(dto.getLength())
                .width(dto.getWidth());

        // relation: airport

        Airport airport = Optional
                .ofNullable(airportService.get(dto.getAirportId()))
                .orElseThrow( () -> new InvalidParameterException("airport_id == " + dto.getAirportId()));

        builder.airport(airport);


        // relation: surface

        RunwaySurface surface = catalogService.findAllRunwaySurfaces()
                .stream()
                .filter(s -> s.getId().equals(dto.getSurfaceId()))
                .findAny()
                .orElseThrow(() -> new InvalidParameterException("surfaceId == " + dto.getSurfaceId()));

        builder.surface(surface);


        // init children

        builder.directions(Lists.newArrayList());

        return builder.build();
    }
}
