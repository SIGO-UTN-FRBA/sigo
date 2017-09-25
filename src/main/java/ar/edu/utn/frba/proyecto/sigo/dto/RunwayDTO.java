package ar.edu.utn.frba.proyecto.sigo.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class RunwayDTO {
    private Long id;
    private Double width;
    private Double length;
    private String surfaceCode;
    private Long airportId;
}
