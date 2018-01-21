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
public class AnalysisObjectDTO {
    private Long id;
    private Long caseId;
    private Long objectId;
    private Boolean included;
    private Integer objectTypeId;
}
