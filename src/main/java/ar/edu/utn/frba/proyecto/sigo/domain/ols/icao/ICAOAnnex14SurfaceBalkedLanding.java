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
@Table(name = "tbl_icao14_surface_balked_landing")
@Data
@Builder
public class ICAOAnnex14SurfaceBalkedLanding extends ICAOAnnex14Surface {

    @Id
    @SequenceGenerator(
            name = "ICAOAnnex14SurfaceBalkedLandingGenerator",
            sequenceName = "seq_icao14_surface_balked_landing",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ICAOAnnex14SurfaceBalkedLandingGenerator"
    )
    @Column
    private Long id;

    @Column(name = "length_of_inner_edge")
    private Double lengthOfInnerEdge;

    @Column(name = "distance_from_threshold")
    private Double distanceFromThreshold;

    @Column
    private Double divergence;

    @Column
    private Double slope;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.BALKED_LANDING_SURFACE;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.BALKED_LANDING_SURFACE.description();
    }
}
