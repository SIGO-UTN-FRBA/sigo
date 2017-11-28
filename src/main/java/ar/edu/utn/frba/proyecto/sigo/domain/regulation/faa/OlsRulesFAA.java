package ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRuleVisitor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="public.tbl_ols_rules_faa")
@PrimaryKeyJoinColumn(name = "rule_id")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OlsRulesFAA extends OlsRule {

    @Override
    public <T> T accept(OlsRuleVisitor<T> visitor) {
        return visitor.visitOlsRuleFAA(this);
    }
}
