package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalyst;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import lombok.*;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true, exclude = "analysisCase")
@Entity
@Table(name = "public.tbl_analysis_surfaces")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisSurface<T extends ObstacleLimitationSurface> extends SigoDomain implements AnalysisRestriction{

    @Id
    @SequenceGenerator(name = "analysisSurfaceGenerator", sequenceName = "ANALYSIS_SURFACE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisSurfaceGenerator")
    @Column(name = "analysis_surface_id")
    private Long id;

    @Any(
            metaDef = "SurfaceMetaDef",
            metaColumn = @Column( name = "surface_type" ),
            fetch = FetchType.EAGER
    )
    @Cascade( { org.hibernate.annotations.CascadeType.ALL })
    @JoinColumn( name = "surface_id")
    private T surface;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysisCase;

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private RunwayDirection direction;

    /*
    @OneToMany(mappedBy = "restriction", cascade = CascadeType.ALL)
    private List<AnalysisObstacle> obstacles = Lists.newArrayList();
    */

    @Override
    public AnalysisRestrictionTypes getRestrictionType() {
        return AnalysisRestrictionTypes.OBSTACLE_LIMITATION_SURFACE;
    }

    @Override
    public String getName() {
        return this.getSurface().getName();
    }

    @Override
    public Geometry getGeometry() {
        return this.getSurface().getGeometry();
    }

    @Override
    public Double determineHeightAt(Point point, OlsAnalyst analyst) {
        return analyst.determineHeightForAnalysisSurface(this, point);
    }
}
