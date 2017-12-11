package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "public.tbl_analysis_surfaces")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisSurface {

    @Id
    @SequenceGenerator(name = "analysisSurfaceGenerator", sequenceName = "ANALYSIS_SURFACE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisSurfaceGenerator")
    @Column(name = "surface_id")
    private Long id;

    private transient ObstacleLimitationSurface surface;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysisCase;

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private RunwayDirection direction;

    @OneToMany(mappedBy = "surface", cascade = CascadeType.REMOVE)
    private List<AnalysisObstacle> obstacles = Lists.newArrayList();

    @Column(name="geom")
    private Geometry geometry;
}
