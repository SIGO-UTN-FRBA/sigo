package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_icao14_surface_takeoff_climb")
@Data
@Builder
public class ICAOAnnex14SurfaceTakeoffClimb extends ICAOAnnex14Surface {

    @Id
    @SequenceGenerator(
            name = "ICAOAnnex14SurfaceTakeoffClimbGenerator",
            sequenceName = "seq_icao14_surface_takeoff_climb",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ICAOAnnex14SurfaceTakeoffClimbGenerator"
    )
    @Column
    private Long id;

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

}
