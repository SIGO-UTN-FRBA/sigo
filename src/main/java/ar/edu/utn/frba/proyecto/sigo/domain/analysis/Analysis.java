package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import com.google.common.base.MoreObjects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "public.tbl_analysis")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class Analysis extends SigoDomain {
    @Id
    @SequenceGenerator(name = "analysisGenerator", sequenceName = "ANALYSIS_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisGenerator")
    @Column(name = "analysis_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "ANALYSIS_PARENT_FK"))
    private Analysis parent;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_id")
    private AnalysisStatuses status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "stage_id")
    private AnalysisStages stage;

    @OneToOne(mappedBy = "analysis", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private AnalysisCase analysisCase;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "edition_date")
    private LocalDateTime editionDate;

    public String toString(){

        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("status", status.name())
                .add("stage", stage.name())
                .add("parent", Optional.ofNullable(parent).map(p -> p.id ).orElse(0L))
                .toString();
    }

    public Boolean isObjectAnalyzed(PlacedObject o) {
        return this.getAnalysisCase().isObjectAnalyzed(o);
    }
}
