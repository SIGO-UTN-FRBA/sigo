package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.dto.object.PlacedObjectDTO;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;

public class PlacedObjectTranslator extends Translator<PlacedObject, PlacedObjectDTO>{



    @Override
    public PlacedObjectDTO getAsDTO(PlacedObject domain) {
        return PlacedObjectDTO.builder()
                    .id(domain.getId())
                    .heightAgl(domain.getHeightAgl())
                    .heightAmls(domain.getHeightAmls())
                    .lighting(domain.getLighting().ordinal())
                    .locationId(domain.getPoliticalLocation().getId())
                    .regionId(domain.getRegion().getId())
                    .markIndicator(domain.getMarkIndicator().ordinal())
                    .name(domain.getName())
                    .ownerId(domain.getOwner().getId())
                    .specId(domain.getSpecId())
                    .type(domain.getType().ordinal())
                    .subtype(domain.getSubtype())
                    .verified(domain.getVerified())
                    .build();
    }

    @Override
    public PlacedObject getAsDomain(PlacedObjectDTO placedObjectDTO) {
        return null;
    }
}
