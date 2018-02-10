package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import com.google.common.collect.Sets;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "public.tbl_analysis_results")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnalysisResult extends SigoDomain<Long> {

    @Id
    @SequenceGenerator(name = "analysisResultGenerator", sequenceName = "ANALYSIS_RESULT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisResultGenerator")
    @Column(name = "result_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "obstacle_id")
    private AnalysisObstacle obstacle;

    @Column(name = "has_adverse_effect")
    private Boolean hasAdverseEffect;

    @ManyToOne
    @JoinColumn(name = "aspect_id")
    private AnalysisAdverseEffectAspect aspect;

    @ManyToMany()
    private Set<AnalysisAdverseEffectMitigation> mitigationMeasures = Sets.newHashSet();

    @Column(name = "allowed")
    private Boolean allowed;

    @Column
    private String extraDetail;
/*
    public String getSummary() {

        if(hasAdverseEffect){

            String aspectPart = "Adverse effect aspect: " + this.getAspect().getName();

            String mitigationPart;

            if(this.getMitigationsMeasures().isEmpty()){
                mitigationPart = "Mitigation measures selected to apply: " + this.getMitigationsMeasures().stream().map(AnalysisAdverseEffectMitigation::getName).collect(Collectors.joining(", "));
            } else {
                mitigationPart = "No mitigation measures was selected to apply.";
            }

            String allowedPart =   (this.allowed)? "It is allowed.": "It is not allowed.";

            return String.format("%s %s %s", aspectPart, mitigationPart, allowedPart);

        }

        return "No adverse effect. It is allowed.";
    }
*/
}
