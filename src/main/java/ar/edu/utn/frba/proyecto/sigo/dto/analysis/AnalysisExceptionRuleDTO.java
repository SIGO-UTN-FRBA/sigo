package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class AnalysisExceptionRuleDTO {

    private Long id;
    private Integer typeId;
    private String name;
    private Long caseId;
    private Long ruleId;
    private Double value;
    private Integer regulationId;
    private Long directionId;

}
