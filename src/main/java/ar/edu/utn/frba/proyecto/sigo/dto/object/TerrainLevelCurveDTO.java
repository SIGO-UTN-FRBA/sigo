package ar.edu.utn.frba.proyecto.sigo.dto.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TerrainLevelCurveDTO {

    private Long id;
    private String name;
    private Integer typeId;
    private Double heightAgl;
    private Double heightAmls;
    private String source;
    private String representation;

}
