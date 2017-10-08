package ar.edu.utn.frba.proyecto.sigo.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "public.tbl_exception_modification_spec")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ExceptionModificationSpec {
    @Id
    @SequenceGenerator(name = "exceptionModificationGenerator", sequenceName = "EXCEPTION_MODIFICATION_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exceptionModificationGenerator")
    @Column(name = "spec_id")
    private Long id;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exception_id")
    private AnalysisException exception;

}