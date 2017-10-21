package ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="public.tbl_OLS_rules_FAA")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OlsRulesFAASpec {

    @Id
    @Column(name="faa_rule_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private OlsRule rule;
}
