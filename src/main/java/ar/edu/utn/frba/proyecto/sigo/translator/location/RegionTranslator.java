package ar.edu.utn.frba.proyecto.sigo.translator.location;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RegionDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RegionTranslator extends SigoTranslator<Region, RegionDTO> {

    @Inject
    public RegionTranslator(Gson objectMapper) {
        super(objectMapper, RegionDTO.class, Region.class);
    }

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
