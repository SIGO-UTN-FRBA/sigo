package ar.edu.utn.frba.proyecto.sigo.dto.airport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class RunwayDistanceDTO {
    private String shortName;
    private String largeName;
    private String description;
    private Double length;
}
