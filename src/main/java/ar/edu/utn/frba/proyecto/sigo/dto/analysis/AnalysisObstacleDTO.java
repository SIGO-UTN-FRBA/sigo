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
    private Long restrictionId;
    private Integer restrictionTypeId;
    private String restrictionName;
    private Long exceptionId;
    private Long directionId;
    private String directionName;
    private Double objectHeight;
    private Double restrictionHeight;
    private Double penetration;
    private ArrayList<Double> coordinate;
    private Boolean excluded;
    private String justification;
    private Long resultId;
    private Boolean allowed;
}
