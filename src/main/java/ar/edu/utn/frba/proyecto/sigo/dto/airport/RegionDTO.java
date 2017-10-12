package ar.edu.utn.frba.proyecto.sigo.dto.airport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class RegionDTO {
    private Long id;
    private String name;
    private String codeFIR;
    private Long stateId;
}
