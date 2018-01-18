package ar.edu.utn.frba.proyecto.sigo.translator.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.TerrainLevelCurve;
import ar.edu.utn.frba.proyecto.sigo.dto.object.TerrainLevelCurveDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.Translator;
import com.google.gson.Gson;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Inject;

public class TerrainLevelCurveTranslator extends Translator<TerrainLevelCurve, TerrainLevelCurveDTO>{

    @Inject
    public TerrainLevelCurveTranslator(
            Gson gson
    ) {
        this.objectMapper = gson;
    }

    @Override
    public TerrainLevelCurveDTO getAsDTO(TerrainLevelCurve domain) {
        return TerrainLevelCurveDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .heightAgl(domain.getHeightAgl())
                .heightAmls(domain.getHeightAmls())
                .typeId(domain.getType().ordinal())
                .source(domain.getSource())
                .representation(domain.getRepresentation())
                .build();
    }

    @Override
    public TerrainLevelCurve getAsDomain(TerrainLevelCurveDTO terrainLevelCurveDTO) {
        throw new NotImplementedException();
    }
}
