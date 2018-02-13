package ar.edu.utn.frba.proyecto.sigo.translator.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.TrackSection;
import ar.edu.utn.frba.proyecto.sigo.domain.object.TrackTypes;
import ar.edu.utn.frba.proyecto.sigo.dto.object.TrackSectionDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TrackSectionTranslator extends SigoTranslator<TrackSection, TrackSectionDTO> {

    @Inject
    public TrackSectionTranslator(Gson objectMapper) {
        super(objectMapper, TrackSectionDTO.class, TrackSection.class);
    }

    @Override
    public TrackSectionDTO getAsDTO(TrackSection domain) {
        return TrackSectionDTO.builder()
                .id(domain.getId())
                .typeId(domain.getType().ordinal())
                .subtypeId(domain.getSubtype().ordinal())
                .name(domain.getName())
                .heightAgl(domain.getHeightAgl())
                .heightAmls(domain.getHeightAmls())
                .verified(domain.getVerified())
                .build();
    }

    @Override
    public TrackSection getAsDomain(TrackSectionDTO dto) {

        TrackSection.TrackSectionBuilder builder = TrackSection.builder();

        return builder
                .id(dto.getId())
                .name(dto.getName())
                .subtype(TrackTypes.values()[dto.getSubtypeId()])
                .heightAgl(dto.getHeightAgl())
                .heightAmls(dto.getHeightAmls())
                .verified(dto.getVerified())
                .build();
    }
}
