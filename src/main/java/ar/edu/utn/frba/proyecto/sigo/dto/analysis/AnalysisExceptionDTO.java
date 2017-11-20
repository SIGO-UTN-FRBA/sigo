package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

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
    private Long id;
    private Integer typeId;
    private String name;
    private Long caseId;

    //AnalysisExceptionRule
    private Long olsRuleId;
    private String property;
    private Double value;

    //AnalysisExceptionSurface
    private String surfaceName;
    private Map<String, Double> properties;
    private Polygon geom;
}
