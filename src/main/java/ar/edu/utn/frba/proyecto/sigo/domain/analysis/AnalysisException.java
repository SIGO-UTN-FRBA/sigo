package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "public.tbl_analysis_exceptions")
@Inheritance(strategy = InheritanceType.JOINED)
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

    @Enumerated(value = EnumType.ORDINAL)
    private AnalysisExceptions type;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysisCase;

    public abstract <T> T accept(AnalysisExceptionVisitor<T> visitor);

}
