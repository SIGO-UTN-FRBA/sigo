package ar.edu.utn.frba.proyecto.sigo.dto.airport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class RunwayDirectionPositionDTO {
    private Integer id;
    private String code;
    private String description;
}
