package ar.edu.utn.frba.proyecto.sigo.translator.location;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RegionDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.Translator;

import javax.inject.Singleton;

@Singleton
public class RegionTranslator extends Translator<Region, RegionDTO> {
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
