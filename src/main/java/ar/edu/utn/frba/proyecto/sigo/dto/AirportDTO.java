package ar.edu.utn.frba.proyecto.sigo.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class AirportDTO {

    private Long id;
    private String nameFIR;
    private String codeFIR;
    private String codeIATA;
    private Long regionId;
    private Long regulationId;
}
