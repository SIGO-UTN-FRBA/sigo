package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true, exclude = "analysisCase")
@Entity
@Table(name = "public.tbl_analysis_obstacles")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisObstacle extends SigoDomain {

    @Id
    @SequenceGenerator(name = "analysisObstacleGenerator", sequenceName = "ANALYSIS_OBSTACLE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisObstacleGenerator")
    @Column(name = "obstacle_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "surface_id")
    private AnalysisSurface surface;

    @ManyToOne
    @JoinColumn(name = "analysis_object_id")
    private AnalysisObject object;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysisCase;

    @ManyToOne
    @JoinColumn(name="exception_id")
    private AnalysisException exception;

    //TODO mas atributos q describan el contexto del obtaculo
}
