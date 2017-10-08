package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.domain.RunwayTakeoffSection;
import ar.edu.utn.frba.proyecto.sigo.dto.RunwayTakeoffSectionDTO;

import javax.inject.Singleton;

@Singleton
public class RunwayTakeoffSectionTranslator extends Translator<RunwayTakeoffSection, RunwayTakeoffSectionDTO>{

    @Override
    public RunwayTakeoffSectionDTO getAsDTO(RunwayTakeoffSection domain) {
        return RunwayTakeoffSectionDTO.builder()
                .id(domain.getId())
                .clearwayLength(domain.getClearwayLength())
                .clearwayWidth(domain.getClearwayWidth())
                .stopwayLength(domain.getStopwayLength())
                .enabled(domain.getEnabled())
                .build();
    }

    @Override
    public RunwayTakeoffSection getAsDomain(RunwayTakeoffSectionDTO runwayTakeoffSectionDTO) {
        return null;
    }
}
