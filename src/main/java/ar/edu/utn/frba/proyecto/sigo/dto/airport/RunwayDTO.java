package ar.edu.utn.frba.proyecto.sigo.dto.airport;

import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class RunwayDTO {
    private Long id;
    private String name;
    private Double width;
    private Double length;
    private Integer surfaceId;
    private Long airportId;
}
