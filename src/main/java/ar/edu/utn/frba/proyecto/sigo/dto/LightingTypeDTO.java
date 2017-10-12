package ar.edu.utn.frba.proyecto.sigo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class LightingTypeDTO {
    private Integer id;
    private String code;
    private String description;
}
