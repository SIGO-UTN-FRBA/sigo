package ar.edu.utn.frba.proyecto.sigo.domain.analysis;


import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;
import lombok.*;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true, exclude = "direction")
@Entity
@Table(name = "public.tbl_analysis_exceptions_rule")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class AnalysisExceptionRule extends AnalysisException {

    @Builder
    public AnalysisExceptionRule(
            Long id,
            String name,
            AnalysisCase analysisCase,
            OlsRule rule,
            Double value,
            Regulations regulation,
            RunwayDirection direction
    ){
        super(id, name, analysisCase);

        this.rule = rule;
        this.value = value;
        this.regulation = regulation;
        this.direction = direction;
    }

    @AnyMetaDef( name= "RuleMetaDef", metaType = "string", idType = "long",
            metaValues = {
                    @MetaValue(value = "OlsRuleICAOAnnex14", targetEntity = OlsRuleICAOAnnex14.class),
            }
    )
    @Any(
            metaDef = "RuleMetaDef",
            metaColumn = @Column( name = "rule_type" )
    )
    @JoinColumn( name = "rule_id" )
    private OlsRule rule;

    @Column(name="value")
    private Double value;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="regulation_id")
    private Regulations regulation;

    @ManyToOne
    @JoinColumn(name = "direction_id", nullable = false)
    private RunwayDirection direction;

    @Override
    public <T> T accept(AnalysisExceptionVisitor<T> visitor) {
        return visitor.visitAnalysisExceptionRule(this);
    }

    @Override
    public AnalysisExceptionTypes getType() {
        return AnalysisExceptionTypes.RULE;
    }
}