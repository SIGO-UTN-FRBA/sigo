package ar.edu.utn.frba.proyecto.sigo.rest.runway;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.commons.rest.Translator;
import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.RunwaySurface;
import ar.edu.utn.frba.proyecto.sigo.dto.RunwayDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RunwayTranslator extends Translator<Runway, RunwayDTO>{

    @Inject
    public RunwayTranslator(
        HibernateUtil hibernateUtil,
        Gson gson
    ){
        this.hibernateUtil = hibernateUtil;
        this.objectMapper = gson;
        this.dtoClass = RunwayDTO.class;
        this.domainClass = Runway.class;
    }

    public RunwayDTO getAsDTO(Runway domain) {

        return RunwayDTO.builder()
                .id(domain.getId())
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

        Airport airport = this.hibernateUtil.doInTransaction(session -> {
            return session.get(Airport.class, dto.getAirportId());
        });

        if(!Optional.ofNullable(airport).isPresent())
            throw new InvalidParameterException("airport_id == " + dto.getAirportId());

        builder.airport(airport);


        // relation: surface

        RunwaySurface surface = this.hibernateUtil.doInTransaction(session -> {
            return session.get(RunwaySurface.class, dto.getSurfaceId());
        });

        if(!Optional.ofNullable(surface).isPresent())
            throw new InvalidParameterException("surfaceId == " + dto.getSurfaceId());

        builder.surface(surface);


        return builder.build();
    }
}
