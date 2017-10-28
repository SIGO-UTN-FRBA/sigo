package ar.edu.utn.frba.proyecto.sigo.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class EnumerationDTO {
    private Integer id;
    private String name;
    private String description;
}
