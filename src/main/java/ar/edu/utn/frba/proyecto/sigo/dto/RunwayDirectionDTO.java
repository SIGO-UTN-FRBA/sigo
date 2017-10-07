package ar.edu.utn.frba.proyecto.sigo.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class RunwayDirectionDTO {
    private Long id;
    private String name;
    private Integer number;
    private Integer position;
    private Long runwayId;
}
