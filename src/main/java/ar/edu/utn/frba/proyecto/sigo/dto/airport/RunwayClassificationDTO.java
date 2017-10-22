package ar.edu.utn.frba.proyecto.sigo.dto.airport;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class RunwayClassificationDTO {

    private Long id;
    private String type;
    private Long directionId;
    private Integer runwayClassification;
    private Integer runwayCategory;
    private Integer aircraftApproachCategory;
    private Integer aircraftClassification;
    private Integer aircraftDesignGroup;
    private Integer runwayTypeNumber;
    private Integer runwayTypeLetter;
}
