package ar.edu.utn.frba.proyecto.sigo.translator.location;

import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.dto.location.PoliticalLocationDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PoliticalLocationTranslator extends SigoTranslator<PoliticalLocation, PoliticalLocationDTO> {

    @Inject
    public PoliticalLocationTranslator(Gson objectMapper) {
        super(objectMapper, PoliticalLocationDTO.class, PoliticalLocation.class);
    }

    @Override
    public PoliticalLocationDTO getAsDTO(PoliticalLocation domain) {
        return PoliticalLocationDTO.builder()
                .id(domain.getId())
                .name(domain.getPath())
                .code(domain.getCode())
                .typeId(domain.getType().getId())
                .build();
    }

    @Override
    public PoliticalLocation getAsDomain(PoliticalLocationDTO politicalLocationDTO) {
        throw new NotImplementedException();
    }
}
