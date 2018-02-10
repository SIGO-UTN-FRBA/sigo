package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import com.vividsolutions.jts.geom.Polygon;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_icao14_surface_balked_landing")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ICAOAnnex14SurfaceBalkedLanding extends ICAOAnnex14Surface <Polygon>
{

    @Column(name = "length_of_inner_edge")
    private Double lengthOfInnerEdge;

    @Column(name = "distance_from_threshold")
    private Double distanceFromThreshold;

    @Column
    private Double divergence;

    @Column
    private Double slope;

    @Column(name = "initial_height")
    private Double initialHeight;

    @Column(name="geom")
    private Polygon geometry;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.BALKED_LANDING_SURFACE;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.BALKED_LANDING_SURFACE.description();
    }

    @Builder
    public ICAOAnnex14SurfaceBalkedLanding(Long id, ICAOAnnex14RunwayClassifications classification, ICAOAnnex14RunwayCategories category, ICAOAnnex14RunwayCodeNumbers code, Polygon geometry, Double lengthOfInnerEdge, Double distanceFromThreshold, Double divergence, Double slope) {
        super(id, classification, category, code);
        this.lengthOfInnerEdge = lengthOfInnerEdge;
        this.distanceFromThreshold = distanceFromThreshold;
        this.divergence = divergence;
        this.slope = slope;
        this.geometry = geometry;
    }
}
