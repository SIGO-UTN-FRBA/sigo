package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalysisSurfaceDTO {

    private Long id;
    private Long caseId;
    private Long directionId;
    private Long surfaceId;
    private ObstacleLimitationSurface surface;
}
