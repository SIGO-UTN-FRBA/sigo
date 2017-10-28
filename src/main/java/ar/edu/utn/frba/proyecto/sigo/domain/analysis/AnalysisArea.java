package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "public.tbl_analysis_areas")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class AnalysisArea {
    @Id
    @SequenceGenerator(name = "analysAreaGenerator", sequenceName = "ANALYS_AREA_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysAreaGenerator")
    @Column(name = "area_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="case_id")
    private AnalysisCase analysisCase;

    @OneToMany(mappedBy = "area", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AnalysisAreaSurface> surfaces;

    @OneToMany(mappedBy = "area", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AnalysisAreaObstacle> obstacles;
}
