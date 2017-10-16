package ar.edu.utn.frba.proyecto.sigo.dto.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class PlacedObjectTypeDTO {
    private Integer id;
    private String code;
    private String description;
}
