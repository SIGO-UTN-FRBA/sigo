package ar.edu.utn.frba.proyecto.sigo.dto.airport;

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
    private Double azimuth;
    private Long runwayId;
    private Double height;
}
