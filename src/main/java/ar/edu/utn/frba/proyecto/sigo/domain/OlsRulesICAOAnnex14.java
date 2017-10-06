package ar.edu.utn.frba.proyecto.sigo.domain;

import javax.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "public.tbl_OLS_rules_ICAOAnnex14")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OlsRulesICAOAnnex14 {
    @Id
    @SequenceGenerator(name = "ruleGenerator", sequenceName = "RULE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ruleGenerator")
    @Column(name = "rule_id")
    private Long id;

    @Column(name = "surface_name")
    private String surfaceName;

    @Column(name = "runway_classification")
    private String runwayClassification;

    @Column(name = "runway_category")
    private String runwayCategory;

    @Column(name = "runway_code_number")
    private String runwayCodeNumber;

    @Column(name = "property")
    private String property;

    @Column(name = "value")
    private Double value;
}
