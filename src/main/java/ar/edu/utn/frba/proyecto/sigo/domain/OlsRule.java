package ar.edu.utn.frba.proyecto.sigo.domain;

import javax.persistence.*;
import java.util.List;
import lombok.*;


@Entity
@Table(name = "public.tbl_ols_rules")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OlsRule extends SigoDomain {

    @Id
    @SequenceGenerator(name = "olsGenerator", sequenceName = "OLS_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "olsGenerator")
    @Column(name = "rule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "regulation_id")
    private Regulation regulation;

    @Column(name = "icao_rule_id")
    private Long icaoRuleId;

    @Column(name = "faa_rule_id")
    private Long faaRuleId;

}


