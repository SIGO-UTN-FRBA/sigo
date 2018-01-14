package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnalysisResultDTO {
    private Long id;
    private Long obstacleId;
    private Boolean obstacle;
    private Boolean keep;
    private String reason;
    private String reasonDetail;
    private Long reasonId;
}
