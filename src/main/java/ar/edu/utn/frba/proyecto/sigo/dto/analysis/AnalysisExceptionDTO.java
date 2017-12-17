package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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

    //AnalysisExceptionSurface
    private Double heightAgl;
    private Geometry geom;

    //AnalysisExceptionDynamicSurface
    private String function;
}
