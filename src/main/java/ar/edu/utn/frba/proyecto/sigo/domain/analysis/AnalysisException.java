package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.*;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true, exclude = "analysisCase")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public abstract class AnalysisException extends SigoDomain {

    @Id
    @SequenceGenerator(name = "analysisExceptionGenerator", sequenceName = "ANALYSIS_EXCEPTION_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisExceptionGenerator")
    @Column(name = "exception_id")
    private Long id;

    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysisCase;

    public abstract <T> T accept(AnalysisExceptionVisitor<T> visitor);

    public abstract AnalysisExceptionTypes getType();
}
