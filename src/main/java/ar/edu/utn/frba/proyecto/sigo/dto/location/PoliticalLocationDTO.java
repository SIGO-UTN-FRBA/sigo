package ar.edu.utn.frba.proyecto.sigo.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class PoliticalLocationDTO {
    private Long id;
    private String name;
    private String code;
    private Long typeId;
}
