package ar.edu.utn.frba.proyecto.sigo.dto.airport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class RunwayApproachSectionDTO {
    private Long id;
    private Double thresholdLength;
    private Double thresholdElevation;
    private Boolean enabled;
    private Long directionId;
}
