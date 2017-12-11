package ar.edu.utn.frba.proyecto.sigo.domain.analysis;


import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "public.tbl_analysis_exceptions_rule")
@PrimaryKeyJoinColumn(name = "exception_id")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class AnalysisExceptionRule extends AnalysisException {

    @Builder
    public AnalysisExceptionRule(
            Long id,
            String name,
            AnalysisExceptions type,
            AnalysisCase analysisCase,
            Long olsRuleId,
            String property,
            Double value,
            Regulations regulation
    ){
        super(id, name, type, analysisCase);

        this.olsRuleId = olsRuleId;
        this.property = property;
        this.value = value;
        this.regulation = regulation;
    }

    @Column(name = "ols_rule_id")
    private Long olsRuleId;

    @Column(name = "property")
    private String property;

    @Column(name="value")
    private Double value;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="regulation_id")
    private Regulations regulation;

    @Override
    public <T> T accept(AnalysisExceptionVisitor<T> visitor) {
        return visitor.visitAnalysisExceptionRule(this);
    }
}