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
@Table(name = "tbl_icao14_surface_approach_second")
@Data
@Builder
public class ICAOAnnex14SurfaceApproachSecondSection extends ICAOAnnex14Surface {

    @Id
    @SequenceGenerator(
            name = "ICAOAnnex14SurfaceApproachSecondSectionGenerator",
            sequenceName = "seq_icao14_surface_approach_second",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ICAOAnnex14SurfaceApproachSecondSectionGenerator"
    )
    @Column
    private Long id;

    @Column
    private Double length;

    @Column
    private Double slope;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.APPROACH_SECOND_SECTION;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.APPROACH_SECOND_SECTION.description();
    }
}
