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
@Table(name = "tbl_icao14_surface_conical")
@Data
@Builder
public class ICAOAnnex14SurfaceConical extends ICAOAnnex14Surface {

    @Id
    @SequenceGenerator(
            name = "ICAOAnnex14SurfaceConicalGenerator",
            sequenceName = "seq_icao14_surface_conical",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ICAOAnnex14SurfaceConicalGenerator"
    )
    @Column
    private Long id;

    @Column
    private Double slope;

    @Column
    private Double height;

    @Column
    private Double ratio;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.CONICAL;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.CONICAL.description();
    }
}
