package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisExceptionVisitor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "public.tbl_exception_modification_spec")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class AnalysisExceptionAvailability extends AnalysisException {
    @Id
    @SequenceGenerator(name = "exceptionModificationGenerator", sequenceName = "EXCEPTION_MODIFICATION_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exceptionModificationGenerator")
    @Column(name = "spec_id")
    private Long id;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exception_id")
    private AnalysisException target;


    @Builder
    public AnalysisExceptionAvailability(
            Long id,
            AnalysisCase analysisCase,
            Boolean enabled,
            AnalysisException target
    ){
        super(id, analysisCase);
        this.enabled = enabled;
        this.target = target;
    }

    @Override
    public <T> T accept(AnalysisExceptionVisitor<T> visitor) {
        return visitor.visitAnalysisExceptionAvailability(this);
    }
}