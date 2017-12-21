package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
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
@Table(name = "tbl_icao14_surface_takeoff_climb")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ICAOAnnex14SurfaceTakeoffClimb extends ICAOAnnex14Surface {

    @Column
    private Double length;

    @Column
    private Double slope;

    @Column(name = "final_Width")
    private Double finalWidth;

    @Column
    private Double divergence;

    @Column(name = "distance_from_runway_ends")
    private Double distanceFromRunwayEnds;

    @Column(name = "length_of_inner_edge")
    private Double lengthOfInnerEdge;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.TAKEOFF_CLIMB;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.TAKEOFF_CLIMB.description();
    }

    @Builder
    public ICAOAnnex14SurfaceTakeoffClimb(Long id, ICAOAnnex14RunwayClassifications classification, ICAOAnnex14RunwayCategories category, ICAOAnnex14RunwayCodeNumbers code, Double length, Double slope, Double finalWidth, Double divergence, Double distanceFromRunwayEnds, Double lengthOfInnerEdge) {
        super(id, classification, category, code);
        this.length = length;
        this.slope = slope;
        this.finalWidth = finalWidth;
        this.divergence = divergence;
        this.distanceFromRunwayEnds = distanceFromRunwayEnds;
        this.lengthOfInnerEdge = lengthOfInnerEdge;
    }
}
