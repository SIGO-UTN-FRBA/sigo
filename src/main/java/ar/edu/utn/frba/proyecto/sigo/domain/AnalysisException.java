package ar.edu.utn.frba.proyecto.sigo.domain;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Polygon;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
