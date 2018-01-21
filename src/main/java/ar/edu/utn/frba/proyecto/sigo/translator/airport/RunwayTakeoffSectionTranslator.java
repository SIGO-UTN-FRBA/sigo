package ar.edu.utn.frba.proyecto.sigo.translator.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayTakeoffSection;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayTakeoffSectionDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RunwayTakeoffSectionTranslator extends SigoTranslator<RunwayTakeoffSection, RunwayTakeoffSectionDTO> {

    private RunwayDirectionService directionService;

    @Inject
    public RunwayTakeoffSectionTranslator(
            Gson gson,
            RunwayDirectionService directionService
    ){
        super(gson, RunwayTakeoffSectionDTO.class, RunwayTakeoffSection.class);

        this.directionService = directionService;
    }

    @Override
    public RunwayTakeoffSectionDTO getAsDTO(RunwayTakeoffSection domain) {
        return RunwayTakeoffSectionDTO.builder()
                .id(domain.getId())
                .clearwayLength(domain.getClearwayLength())
                .clearwayWidth(domain.getClearwayWidth())
                .stopwayLength(domain.getStopwayLength())
                .enabled(domain.getEnabled())
                .directionId(domain.getRunwayDirection().getId())
                .build();
    }

    @Override
    public RunwayTakeoffSection getAsDomain(RunwayTakeoffSectionDTO dto) {

        RunwayTakeoffSection.RunwayTakeoffSectionBuilder builder = RunwayTakeoffSection.builder();

        // basic properties
        builder
                .id(dto.getId())
                .clearwayLength(dto.getClearwayLength())
                .clearwayWidth(dto.getClearwayWidth())
                .stopwayLength(dto.getStopwayLength())
                .enabled(dto.getEnabled());

        // relation: direction
        RunwayDirection direction = directionService.get(dto.getDirectionId());
        if(!Optional.ofNullable(direction).isPresent())
            throw new InvalidParameterException("directionId == " + dto.getDirectionId());

        builder.runwayDirection(direction);

        return builder.build();
    }
}
