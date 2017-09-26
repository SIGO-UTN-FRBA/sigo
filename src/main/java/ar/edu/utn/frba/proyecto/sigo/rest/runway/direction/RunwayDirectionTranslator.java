package ar.edu.utn.frba.proyecto.sigo.rest.runway.direction;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.commons.rest.Translator;
import ar.edu.utn.frba.proyecto.sigo.domain.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.dto.RunwayDirectionDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import com.google.gson.Gson;

import javax.inject.Inject;
import java.util.Optional;

public class RunwayDirectionTranslator extends Translator<RunwayDirection, RunwayDirectionDTO>{

    @Inject
    public RunwayDirectionTranslator(
            HibernateUtil hibernateUtil,
            Gson gson
    ){
        this.hibernateUtil = hibernateUtil;
        this.objectMapper = gson;
        this.dtoClass = RunwayDirectionDTO.class;
        this.domainClass = RunwayDirection.class;
    }

    @Override
    public RunwayDirectionDTO getAsDTO(RunwayDirection domain) {
        return RunwayDirectionDTO.builder()
                .id(domain.getId())
                .code(domain.getCode())
                .runwayId(domain.getRunway().getId())
                .available(domain.getAvailable())
                .build();
    }

    @Override
    public RunwayDirection getAsDomain(RunwayDirectionDTO dto) {
        RunwayDirection.RunwayDirectionBuilder builder = RunwayDirection.builder();

        // basic properties
        builder
                .id(dto.getId())
                .code(dto.getCode())
                .available(dto.getAvailable());

        // relation: runway

        Runway runway = this.hibernateUtil.doInTransaction(session -> {
            return session.get(Runway.class, dto.getRunwayId());
        });

        if(!Optional.ofNullable(runway).isPresent())
            throw new InvalidParameterException("ruwnay_id == " + dto.getRunwayId());

        builder.runway(runway);

        return builder.build();
    }
}
