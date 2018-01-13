package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import lombok.Data;

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
@Table(name = "public.tbl_analysis_results")
@Data
public class AnalysisResult {

    @Id
    @SequenceGenerator(name = "analysisResultGenerator", sequenceName = "ANALYSIS_RESULT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisResultGenerator")
    @Column(name = "result_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "obstacle_id")
    private AnalysisObstacle obstacle;

    @Column(name = "is_obstacle")
    private Boolean isObstacle;

    @Column(name = "must_keep")
    private Boolean mustKeep;

    @ManyToOne
    @JoinColumn(name = "reason_id")
    private AnalysisResultReason reason;

    @Column
    private String reasonDetail;

}
