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
@Table(name = "tbl_icao14_surface_inner_transitional")
@Data
@Builder
public class ICAOAnnex14SurfaceInnerTransitional extends ICAOAnnex14Surface {

    @Id
    @SequenceGenerator(
            name = "ICAOAnnex14SurfaceInnerTransitionalGenerator",
            sequenceName = "seq_icao14_surface_inner_transitional",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ICAOAnnex14SurfaceInnerTransitionalGenerator"
    )
    @Column
    private Long id;

    @Column
    private Double slope;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.INNER_TRANSITIONAL;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.INNER_TRANSITIONAL.description();
    }
}
