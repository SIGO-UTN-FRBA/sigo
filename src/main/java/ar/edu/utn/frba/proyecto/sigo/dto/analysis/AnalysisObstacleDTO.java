package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalysisObstacleDTO {
    private Long id;
    private Long objectId;
    private Long caseId;
    private Long surfaceId;
    private Long exceptionId;
}
