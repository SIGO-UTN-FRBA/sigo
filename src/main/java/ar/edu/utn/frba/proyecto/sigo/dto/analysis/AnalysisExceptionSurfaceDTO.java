package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import com.vividsolutions.jts.geom.Polygon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class AnalysisExceptionSurfaceDTO {

    private Long id;
    private Integer typeId;
    private String name;
    private Long caseId;
    private Double heightAmls;
    private Polygon geom;

}
