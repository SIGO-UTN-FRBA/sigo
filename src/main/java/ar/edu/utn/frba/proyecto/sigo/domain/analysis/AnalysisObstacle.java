package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.*;
import org.hibernate.annotations.Any;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true, exclude = {"analysisCase", "object", "result", "restriction"})
@Entity
@Table(name = "public.tbl_analysis_obstacles")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisObstacle extends SigoDomain<Long> {

    @Id
    @SequenceGenerator(name = "analysisObstacleGenerator", sequenceName = "ANALYSIS_OBSTACLE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisObstacleGenerator")
    @Column(name = "obstacle_id")
    private Long id;

    @Any(
            metaDef = "RestrictionMetaDef",
            metaColumn = @Column( name = "restriction_type" ),
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "restriction_id")
    private AnalysisRestriction restriction;

    @ManyToOne
    @JoinColumn(name = "analysis_object_id")
    private AnalysisObject object;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysisCase;

    @Column(name = "object_height_amls")
    private Double objectHeight;

    @Column(name = "restriction_height_amls")
    private Double restrictionHeight;

    @OneToOne (mappedBy = "obstacle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private AnalysisResult result;

    @Column
    private Boolean excepting;

    public Double getPenetration(){
        return objectHeight - restrictionHeight;
    }

    public Boolean isCausedBy(AnalysisRestriction restriction) {
        return this.getRestriction().equals(restriction);
    }
}
