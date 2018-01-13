package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisResult;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisResultDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.Translator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;

public class AnalysisResultTranslator extends Translator<AnalysisResult,AnalysisResultDTO>{

    @Override
    public AnalysisResultDTO getAsDTO(AnalysisResult domain) {

        AnalysisResultDTO.AnalysisResultDTOBuilder builder = AnalysisResultDTO.builder();

        builder
                .id(domain.getId())
                .keep(domain.getMustKeep())
                .obstacle(domain.getIsObstacle())
                .reasonDetail(domain.getReasonDetail())
                .obstacleId(domain.getId());

        Optional.ofNullable(domain.getReason())
                .ifPresent(r -> builder.reason(r.getDescription()).reasonId(r.getId()));

        return builder.build();
    }

    @Override
    public AnalysisResult getAsDomain(AnalysisResultDTO analysisResultDTO) {
        throw new NotImplementedException();
    }
}
