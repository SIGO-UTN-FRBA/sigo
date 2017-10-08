package ar.edu.utn.frba.proyecto.sigo.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "public.tbl_exception_rule_value_spec")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ExceptionRuleValueSpec {
    @Id
    @SequenceGenerator(name = "exceptionRuleValueSpecGenerator", sequenceName = "EXCEPTION_RULE_VALUE_SPEC_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exceptionRuleValueSpecGenerator")
    @Column(name = "spec_id")
    private Long id;

    @Column(name = "ols_rule_id")
    private Long olsRuleId;

    @Column(name = "property")
    private String property;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exception_id")
    private AnalysisException exception;

}