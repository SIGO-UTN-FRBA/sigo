package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class AnalysisExceptionDynamicSurfaceDTO {

    private Long id;
    private Integer typeId;
    private String name;
    private Long caseId;
    private String function;
}
