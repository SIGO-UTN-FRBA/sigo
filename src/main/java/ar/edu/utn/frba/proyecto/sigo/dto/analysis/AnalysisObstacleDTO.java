package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalysisObstacleDTO {
    private Long id;
    private Long objectId;
    private String objectName;
    private Integer objectType;
    private Long caseId;
    private Long surfaceId;
    private String surfaceName;
    private Long exceptionId;
    private Long directionId;
    private String directionName;
    private Double objectHeight;
    private Double surfaceHeight;
    private Double penetration;
    private ArrayList<Double> coordinate;
    private Boolean excluded;
    private String justification;
    private Long resultId;
}
