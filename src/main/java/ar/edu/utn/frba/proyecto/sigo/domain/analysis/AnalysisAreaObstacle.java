package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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

@Entity
@Table(name = "public.tbl_analysis_area_obstacles")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisAreaObstacle extends SigoDomain {

    @Id
    @SequenceGenerator(name = "analysisAreaObstacleGenerator", sequenceName = "ANALYSIS_AREA_OBSTACLE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisAreaObstacleGenerator")
    @Column(name = "obstacle_id")
    private Long id;

    @ManyToOne
    private AnalysisArea area;

    @OneToOne
    @JoinColumn(name = "analysis_object_id")
    private AnalysisObject analysisObject;
}
