package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import lombok.Builder;
import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_icao14_surface_inner_approach")
@Data
@Builder
public class ICAOAnnex14SurfaceInnerApproach extends ICAOAnnex14Surface{

    @Id
    @SequenceGenerator(
            name = "ICAOAnnex14SurfaceInnerApproachGenerator",
            sequenceName = "seq_icao14_surface_inner_approach",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ICAOAnnex14SurfaceInnerApproachGenerator"
    )
    @Column
    private Long id;

    @Column
    private Double width;

    @Column(name = "distance_from_threshold")
    private Double distanceFromThreshold;

    @Column
    private Double length;

    @Column
    private Double slope;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.INNER_APPROACH;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.INNER_APPROACH.description();
    }
}
