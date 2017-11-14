package ar.edu.utn.frba.proyecto.sigo.dto.airport;
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
    private String codeLocal;
    private Long regionId;
    private Integer regulationId;
}
