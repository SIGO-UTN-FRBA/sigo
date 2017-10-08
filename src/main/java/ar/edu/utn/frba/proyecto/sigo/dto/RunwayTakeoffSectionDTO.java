package ar.edu.utn.frba.proyecto.sigo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class RunwayTakeoffSectionDTO {
    private Long id;
    private Boolean enabled;
    private Double clearwayLength;
    private Double clearwayWidth;
    private Double stopwayLength;
    private Long directionId;
}
