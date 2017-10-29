package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisCaseDTO {
    private Long id;
    private Long status;
    private Long airportId;
    private Integer regulationId;
    private Long areaId;

}
