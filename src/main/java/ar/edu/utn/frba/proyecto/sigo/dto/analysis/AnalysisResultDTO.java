package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnalysisResultDTO {
    private Long id;
    private Long obstacleId;
    private Boolean hasAdverseEffect;
    private Boolean allowed;
    private Long aspectId;
    private String extraDetail;
    private List<Long> mitigationMeasuresIds;
}
