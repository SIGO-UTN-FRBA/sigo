package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import com.vividsolutions.jts.geom.Polygon;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_icao14_surface_inner_approach")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ICAOAnnex14SurfaceInnerApproach
    extends ICAOAnnex14Surface
    implements ObstacleLimitationSurface<Polygon>
{

    @Column
    private Double width;

    @Column(name = "distance_from_threshold")
    private Double distanceFromThreshold;

    @Column
    private Double length;

    @Column
    private Double slope;

    @Column(name="geom")
    private Polygon geometry;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.INNER_APPROACH;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.INNER_APPROACH.description();
    }

    @Builder
    public ICAOAnnex14SurfaceInnerApproach(Long id, ICAOAnnex14RunwayClassifications classification, ICAOAnnex14RunwayCategories category, ICAOAnnex14RunwayCodeNumbers code, Polygon geometry, Double width, Double distanceFromThreshold, Double length, Double slope) {
        super(id, classification, category, code);
        this.width = width;
        this.distanceFromThreshold = distanceFromThreshold;
        this.length = length;
        this.slope = slope;
    }
}
