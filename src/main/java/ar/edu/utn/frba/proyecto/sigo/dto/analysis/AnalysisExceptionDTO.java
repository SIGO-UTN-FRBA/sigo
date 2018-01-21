package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import com.vividsolutions.jts.geom.Polygon;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisExceptionDTO {
    //AnalysisException
    private Long id;
    private Integer typeId;
    private String name;
    private Long caseId;

    //AnalysisExceptionRule
    private Long ruleId;
    private Double value;
    private Integer regulationId;
    private Long directionId;

    //AnalysisExceptionSurface
    private Double heightAmls;
    private Polygon geom;

    //AnalysisExceptionDynamicSurface
    private String function;
}
