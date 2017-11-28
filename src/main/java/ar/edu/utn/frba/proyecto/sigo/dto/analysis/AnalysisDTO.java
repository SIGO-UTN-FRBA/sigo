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
public class AnalysisDTO {
    private Long id;
    private Long caseId;
    private Long parentId;
    private Long creationDate;
    private Long editionDate;
    private Integer stageId;
    private Integer statusId;
    private Long airportId;
    private Integer regulationId;
}
