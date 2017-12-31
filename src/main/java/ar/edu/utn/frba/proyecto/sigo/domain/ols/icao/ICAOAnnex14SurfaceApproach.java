package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import com.vividsolutions.jts.geom.Polygon;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_icao14_surface_approach")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ICAOAnnex14SurfaceApproach extends ICAOAnnex14Surface {

    @Column(name = "length_of_inner_edge")
    private Double lengthOfInnerEdge;

    @Column(name = "distance_from_threshold")
    private Double distanceFromThreshold;

    @Column
    private Double divergence;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.APPROACH;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.APPROACH.description();
    }

    @Builder
    public ICAOAnnex14SurfaceApproach(Long id, ICAOAnnex14RunwayClassifications classification, ICAOAnnex14RunwayCategories category, ICAOAnnex14RunwayCodeNumbers code, Polygon geometry, Double lengthOfInnerEdge, Double distanceFromThreshold, Double divergence) {
        super(id, classification, category, code, geometry);
        this.lengthOfInnerEdge = lengthOfInnerEdge;
        this.distanceFromThreshold = distanceFromThreshold;
        this.divergence = divergence;
    }
}