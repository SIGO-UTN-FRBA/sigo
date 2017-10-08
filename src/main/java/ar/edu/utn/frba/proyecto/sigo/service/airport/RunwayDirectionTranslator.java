package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirectionPositions;
import ar.edu.utn.frba.proyecto.sigo.dto.RunwayDirectionDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RunwayDirectionTranslator extends Translator<RunwayDirection, RunwayDirectionDTO> {

    private RunwayService runwayService;

    @Inject
    public RunwayDirectionTranslator(
            Gson gson,
            RunwayService runwayService
    ){
        this.runwayService = runwayService;
        this.objectMapper = gson;
        this.dtoClass = RunwayDirectionDTO.class;
        this.domainClass = RunwayDirection.class;
    }

    @Override
    public RunwayDirectionDTO getAsDTO(RunwayDirection domain) {
        return RunwayDirectionDTO.builder()
                .id(domain.getId())
                .number(domain.getNumber())
                .position(domain.getPosition().ordinal())
                .runwayId(domain.getRunway().getId())
                .name(domain.getIdentifier())
                .build();
    }

    @Override
    public RunwayDirection getAsDomain(RunwayDirectionDTO dto) {
        RunwayDirection.RunwayDirectionBuilder builder = RunwayDirection.builder();

        // basic properties
        builder
                .id(dto.getId())
                .number(dto.getNumber())
                .position(RunwayDirectionPositions.getEnum(dto.getPosition()));

        // relation: runway

        Runway runway = runwayService.get(dto.getRunwayId());

        if(!Optional.ofNullable(runway).isPresent())
            throw new InvalidParameterException("ruwnay_id == " + dto.getRunwayId());

        builder.runway(runway);

        return builder.build();
    }
}
