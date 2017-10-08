package ar.edu.utn.frba.proyecto.sigo.domain.analysis;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "public.tbl_analysis_exceptions")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class AnalysisException {

    @Id
    @SequenceGenerator(name = "analysExceptionGenerator", sequenceName = "ANALYS_EXCEPTION_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysExceptionGenerator")
    @Column(name = "exception_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysiscase;

    @OneToOne(mappedBy = "exception",cascade = CascadeType.REMOVE)
    private ExceptionRuleValueSpec valueSpec;

    @OneToOne(mappedBy = "exception",cascade = CascadeType.REMOVE)
    private ExceptionSurfaceSpec surfaceSpec;

    @OneToOne(mappedBy = "exception",cascade = CascadeType.REMOVE)
    private ExceptionModificationSpec modificationSpec;
}
