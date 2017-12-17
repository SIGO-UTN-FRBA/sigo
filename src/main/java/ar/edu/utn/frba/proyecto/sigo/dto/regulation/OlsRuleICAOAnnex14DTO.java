package ar.edu.utn.frba.proyecto.sigo.dto.regulation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OlsRuleICAOAnnex14DTO extends OlsRuleDTO {

    private Integer surface;
    private Integer runwayClassification;
    private Integer runwayCategory;
    private Integer runwayCodeNumber;
    private String propertyName;
    private String propertyCode;
    private Double value;

    @Builder
    public OlsRuleICAOAnnex14DTO(Long id, Long ruleId, Integer regulationId, Integer surface, Integer runwayClassification, Integer runwayCategory, Integer runwayCodeNumber, String propertyName, String propertyCode, Double value) {

        super(id, ruleId, regulationId);

        this.surface = surface;
        this.runwayClassification = runwayClassification;
        this.runwayCategory = runwayCategory;
        this.runwayCodeNumber = runwayCodeNumber;
        this.propertyName = propertyName;
        this.propertyCode = propertyCode;
        this.value = value;
    }
}
