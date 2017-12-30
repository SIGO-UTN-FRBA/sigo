package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisObstacleDTO;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;

public class AnalysisObstacleTranslator extends Translator<AnalysisObstacle, AnalysisObstacleDTO> {

    @Override
    public AnalysisObstacleDTO getAsDTO(AnalysisObstacle domain) {
        AnalysisObstacleDTO.AnalysisObstacleDTOBuilder builder = AnalysisObstacleDTO.builder();

        builder
            .id(domain.getId())
            .caseId(domain.getAnalysisCase().getId())
            .objectId(domain.getObject().getId());

        Optional.ofNullable(domain.getSurface())
                .ifPresent(s -> builder.surfaceId(s.getId()));

        Optional.ofNullable(domain.getException())
                .ifPresent(e -> builder.exceptionId(e.getId()));

        return builder.build();
    }

    @Override
    public AnalysisObstacle getAsDomain(AnalysisObstacleDTO analysisObstacleDTO) {
        throw new NotImplementedException();
    }
}
