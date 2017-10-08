package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.domain.RunwayApproachSection;
import ar.edu.utn.frba.proyecto.sigo.dto.RunwayApproachSectionDTO;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RunwayApproachSectionTranslator extends Translator<RunwayApproachSection,RunwayApproachSectionDTO>{

    private final RunwayApproachSectionService sectionService;

    @Inject
    public RunwayApproachSectionTranslator(
            Gson gson,
            RunwayApproachSectionService sectionService
    ){
        this.sectionService = sectionService;
        this.objectMapper = gson;
        this.dtoClass = RunwayApproachSectionDTO.class;
        this.domainClass = RunwayApproachSection.class;
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
    public RunwayApproachSection getAsDomain(RunwayApproachSectionDTO runwayApproachSectionDTO) {
        return null;
    }
}
