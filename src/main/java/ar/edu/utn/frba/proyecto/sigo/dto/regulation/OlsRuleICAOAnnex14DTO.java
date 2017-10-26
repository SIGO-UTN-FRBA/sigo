package ar.edu.utn.frba.proyecto.sigo.dto.regulation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class OlsRuleICAOAnnex14DTO {

    private Long id;
    private Long ruleId;
    private Integer surface;
    private Integer runwayClassification;
    private Integer runwayCategory;
    private Integer runwayCodeNumber;
    private String property;
    private Double value;
}
