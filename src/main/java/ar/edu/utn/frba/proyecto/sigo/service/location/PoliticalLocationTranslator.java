package ar.edu.utn.frba.proyecto.sigo.service.location;

import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.dto.location.PoliticalLocationDTO;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PoliticalLocationTranslator extends Translator<PoliticalLocation, PoliticalLocationDTO>{
    @Override
    public PoliticalLocationDTO getAsDTO(PoliticalLocation domain) {
        return PoliticalLocationDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .code(domain.getCode())
                .typeId(domain.getType().getId())
                .build();
    }

    @Override
    public PoliticalLocation getAsDomain(PoliticalLocationDTO politicalLocationDTO) {
        throw new NotImplementedException();
    }
}