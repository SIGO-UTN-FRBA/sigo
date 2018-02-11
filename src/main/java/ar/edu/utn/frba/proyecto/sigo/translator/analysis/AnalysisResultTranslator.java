package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisResult;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisResultDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.MissingParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisAdverseEffectAspectService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisAdverseEffectMitigationService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisObstacleService;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class AnalysisResultTranslator extends SigoTranslator<AnalysisResult,AnalysisResultDTO> {

    private AnalysisAdverseEffectAspectService aspectService;
    private AnalysisAdverseEffectMitigationService mitigationService;
    private AnalysisObstacleService obstacleService;

    @Inject
    public AnalysisResultTranslator(
            Gson gson,
            AnalysisAdverseEffectAspectService aspectService,
            AnalysisAdverseEffectMitigationService mitigationService,
            AnalysisObstacleService obstacleService
    ){
        super(gson, AnalysisResultDTO.class, AnalysisResult.class);

        this.aspectService = aspectService;
        this.mitigationService = mitigationService;
        this.obstacleService = obstacleService;
    }

    @Override
    public AnalysisResultDTO getAsDTO(AnalysisResult domain) {

        AnalysisResultDTO.AnalysisResultDTOBuilder builder = AnalysisResultDTO.builder();

        builder
                .id(domain.getId())
                .hasAdverseEffect(domain.getHasAdverseEffect())
                .allowed(domain.getAllowed())
                .obstacleId(domain.getObstacle().getId())
                .extraDetail(domain.getExtraDetail());

        Optional.ofNullable(domain.getAspect()).ifPresent(a -> builder.aspectId(a.getId()));

        Optional.ofNullable(domain.getMitigationMeasures()).ifPresent( list -> builder.mitigationMeasuresIds(list.stream().map(m -> m.getId()).collect(Collectors.toList())));

        return builder.build();
    }

    @Override
    public AnalysisResult getAsDomain(AnalysisResultDTO dto) {

        AnalysisResult.AnalysisResultBuilder builder = AnalysisResult.builder();

        //base attributes
        builder
                .id(dto.getId())
                .hasAdverseEffect(dto.getHasAdverseEffect())
                .allowed(dto.getAllowed())
                .extraDetail(dto.getExtraDetail());

        //relation: aspect
        Optional.ofNullable(dto.getAspectId()).ifPresent( id -> builder.aspect(aspectService.get(id)));

        //relation: mitigations
        Optional.ofNullable(dto.getMitigationMeasuresIds()).ifPresent( ids ->
            builder.mitigationMeasures(ids.stream().map(id -> mitigationService.get(id)).collect(Collectors.toSet()))
        );

        //relation: obstacle
        AnalysisObstacle obstacle = Optional.ofNullable(obstacleService.get(dto.getObstacleId()))
                .orElseThrow(() -> new MissingParameterException("obstacle_id is required"));
        builder.obstacle(obstacle);

        return builder.build();
    }
}
