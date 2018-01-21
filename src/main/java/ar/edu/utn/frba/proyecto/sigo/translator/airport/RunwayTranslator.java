package ar.edu.utn.frba.proyecto.sigo.translator.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwaySurfaces;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.airport.AirportService;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RunwayTranslator extends SigoTranslator<Runway, RunwayDTO> {

    private AirportService airportService;

    @Inject
    public RunwayTranslator(
        Gson gson,
        AirportService airportService
    ){
        super(gson, RunwayDTO.class, Runway.class);

        this.airportService = airportService;
    }

    public RunwayDTO getAsDTO(Runway domain) {

        return RunwayDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .length(domain.getLength())
                .width(domain.getWidth())
                .surfaceId(domain.getSurface().ordinal())
                .airportId(domain.getAirport().getId())
                .build();
    }

    public Runway getAsDomain(RunwayDTO dto) {

        Runway.RunwayBuilder builder = Runway.builder();

        // basics attributes
        builder
                .id(dto.getId())
                .length(dto.getLength())
                .width(dto.getWidth())
                .surface(RunwaySurfaces.values()[dto.getSurfaceId()]);

        // relation: airport
        Airport airport = Optional
                .ofNullable(airportService.get(dto.getAirportId()))
                .orElseThrow( () -> new InvalidParameterException("airport_id == " + dto.getAirportId()));

        builder.airport(airport);

        // init children
        builder.directions(Lists.newArrayList());

        return builder.build();
    }
}
