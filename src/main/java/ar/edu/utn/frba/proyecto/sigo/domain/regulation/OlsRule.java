package ar.edu.utn.frba.proyecto.sigo.domain.regulation;

import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "public.tbl_ols_rules")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public abstract class OlsRule extends SigoDomain {

    @Id
    @SequenceGenerator(name = "olsRuleGenerator", sequenceName = "OLS_RULE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "olsRuleGenerator")
    @Column(name = "rule_id")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "regulation_id")
    private Regulations regulation;

    public abstract<T> T accept(OlsRuleVisitor<T> visitor);
}


