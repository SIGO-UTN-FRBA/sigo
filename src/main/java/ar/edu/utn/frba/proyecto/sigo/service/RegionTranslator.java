package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.domain.Region;
import ar.edu.utn.frba.proyecto.sigo.dto.RegionDTO;

public class RegionTranslator extends Translator<Region, RegionDTO>{
    @Override
    public RegionDTO getAsDTO(Region domain) {
        return RegionDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .codeFIR(domain.getCodeFIR())
                .build();
    }

    @Override
    public Region getAsDomain(RegionDTO regionDTO) {
        return null;
    }
}