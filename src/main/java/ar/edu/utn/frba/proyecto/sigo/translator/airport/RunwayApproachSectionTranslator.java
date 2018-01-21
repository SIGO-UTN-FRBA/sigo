package ar.edu.utn.frba.proyecto.sigo.translator.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayApproachSection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayApproachSectionDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RunwayApproachSectionTranslator extends SigoTranslator<RunwayApproachSection,RunwayApproachSectionDTO> {

    private RunwayDirectionService directionService;

    @Inject
    public RunwayApproachSectionTranslator(
            Gson gson,
            RunwayDirectionService directionService
    ){
        super(gson, RunwayApproachSectionDTO.class, RunwayApproachSection.class);

        this.directionService = directionService;
    }

    @Override
    public RunwayApproachSectionDTO getAsDTO(RunwayApproachSection domain) {
        return RunwayApproachSectionDTO.builder()
                .id(domain.getId())
                .directionId(domain.getRunwayDirection().getId())
                .enabled(domain.getEnabled())
                .thresholdElevation(domain.getThresholdElevation())
                .thresholdLength(domain.getThresholdLength())
                .build();
    }

    @Override
    public RunwayApproachSection getAsDomain(RunwayApproachSectionDTO dto) {

        RunwayApproachSection.RunwayApproachSectionBuilder builder = RunwayApproachSection.builder();

        // basic properties
        builder
                .id(dto.getId())
                .thresholdElevation(dto.getThresholdElevation())
                .thresholdLength(dto.getThresholdLength())
                .enabled(dto.getEnabled());

        // relation: direction
        RunwayDirection direction = directionService.get(dto.getDirectionId());
        if(!Optional.ofNullable(direction).isPresent())
            throw new InvalidParameterException("directionId == " + dto.getDirectionId());

        builder.runwayDirection(direction);

        return builder.build();
    }
}
