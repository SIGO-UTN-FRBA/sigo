package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.object.ElevatedObject;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import com.google.common.base.MoreObjects;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true, exclude = "analysisCase")
@Entity
@Table(name = "public.tbl_analysis")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class Analysis extends SigoDomain {
    @Id
    @SequenceGenerator(
            name = "analysisGenerator",
            sequenceName = "ANALYSIS_SEQUENCE",
            allocationSize = 1,
            initialValue = 1000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "analysisGenerator"
    )
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name= "regulation_id")
    private Regulations regulation;

    public String toString(){

        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("status", status.name())
                .add("stage", stage.name())
                .add("regulation", regulation.description())
                .add("parent", Optional.ofNullable(parent).map(p -> p.id ).orElse(0L))
                .toString();
    }

    public Boolean hasAlreadyBeenAnalyzed(ElevatedObject o) {
        return this.getAnalysisCase().hasAlreadyBeenAnalyzed(o);
    }
}
