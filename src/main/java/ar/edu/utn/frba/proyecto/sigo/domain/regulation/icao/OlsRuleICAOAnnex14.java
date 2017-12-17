package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surfaces;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRuleVisitor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "public.tbl_ols_rules_icaoannex14")
@PrimaryKeyJoinColumn(name = "rule_id")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OlsRuleICAOAnnex14 extends OlsRule {

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "surface_name")
    private ICAOAnnex14Surfaces surface;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "runway_classification")
    private ICAOAnnex14RunwayClassifications runwayClassification;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "runway_category")
    private ICAOAnnex14RunwayCategories runwayCategory;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "runway_code_number")
    private ICAOAnnex14RunwayCodeNumbers runwayCodeNumber;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "value")
    private Double value;

    @Column(name = "property_code")
    private String propertyCode;

    @Override
    public <T> T accept(OlsRuleVisitor<T> visitor) {
        return visitor.visitOlsRuleICAOAnnex14(this);
    }
}
