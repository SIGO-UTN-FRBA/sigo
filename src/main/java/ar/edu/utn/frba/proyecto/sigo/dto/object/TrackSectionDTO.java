package ar.edu.utn.frba.proyecto.sigo.dto.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class TrackSectionDTO {
    private Long id;
    private String name;
    private Integer typeId;
    private Integer subtypeId;
    private Double heightAgl;
    private Double heightAmls;
    private Boolean verified;
}
