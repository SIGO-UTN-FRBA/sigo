package ar.edu.utn.frba.proyecto.sigo.rest.runway;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.commons.rest.Translator;
import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.Runway;
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
                .surfaceCode("")
                .airportId(domain.getAirport().getId())
                .build();
    }

    public Runway getAsDomain(RunwayDTO runwayDTO) {

        Runway.RunwayBuilder builder = Runway.builder();

        builder
                .id(runwayDTO.getId())
                .length(runwayDTO.getLength())
                .width(runwayDTO.getWidth());

        Airport airport = this.hibernateUtil.doInTransaction(session -> {
            return session.get(Airport.class, runwayDTO.getAirportId());
        });

        if(!Optional.ofNullable(airport).isPresent())
            throw new InvalidParameterException("airport_id == " + runwayDTO.getAirportId());

        builder.airport(airport);

        return builder.build();
    }
}
