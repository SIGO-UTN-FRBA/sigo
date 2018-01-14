package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisResult;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisResultReason;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisResultDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisObstacleService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisResultReasonService;
import ar.edu.utn.frba.proyecto.sigo.translator.Translator;
import com.google.gson.Gson;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import java.util.Optional;

public class AnalysisResultTranslator extends Translator<AnalysisResult,AnalysisResultDTO>{

    private final AnalysisResultReasonService reasonService;
    private final AnalysisObstacleService obstacleService;

    @Inject
    public AnalysisResultTranslator(
            Gson gson,
            AnalysisResultReasonService reasonService,
            AnalysisObstacleService obstacleService
    ){
        this.reasonService = reasonService;
        this.obstacleService = obstacleService;
        this.objectMapper = gson;
        this.domainClass = AnalysisResult.class;
        this.dtoClass = AnalysisResultDTO.class;
    }

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
    public AnalysisResult getAsDomain(AnalysisResultDTO dto) {

        AnalysisResult.AnalysisResultBuilder builder = AnalysisResult.builder();

        //base attributes
        builder
                .id(dto.getId())
                .isObstacle(dto.getObstacle())
                .mustKeep(dto.getKeep())
                .reasonDetail(dto.getReasonDetail());

        //relation: reason
        AnalysisResultReason reason = Optional.ofNullable(reasonService.get(dto.getReasonId()))
                .orElseThrow(() -> new InvalidParameterException("region_id == " + dto.getReasonId()));
        builder.reason(reason);

        //relation: obstacle
        AnalysisObstacle obstacle = Optional.ofNullable(obstacleService.get(dto.getObstacleId()))
                .orElseThrow(() -> new InvalidParameterException("obstacle_id == " + dto.getObstacleId()));
        builder.obstacle(obstacle);

        return builder.build();
    }
}
