package ar.edu.utn.frba.proyecto.sigo.domain.regulation;

import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.OlsRulesFAASpec;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRulesICAOAnnex14Spec;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;


@Entity
@Table(name = "public.tbl_ols_rules")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OlsRule extends SigoDomain {

    @Id
    @SequenceGenerator(name = "olsRuleGenerator", sequenceName = "OLS_RULE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "olsRuleGenerator")
    @Column(name = "rule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "regulation_id")
    private Regulation regulation;

    @OneToOne(
            mappedBy = "rule",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @LazyToOne( LazyToOneOption.NO_PROXY )
    private OlsRulesICAOAnnex14Spec icaoRule;

    @OneToOne(
            mappedBy = "rule",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @LazyToOne( LazyToOneOption.NO_PROXY )
    private OlsRulesFAASpec faaRule;

}


