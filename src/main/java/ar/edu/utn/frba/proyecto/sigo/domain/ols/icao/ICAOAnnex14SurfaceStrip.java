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
@Table(name = "tbl_icao14_surface_strip")
@Data
@Builder
public class ICAOAnnex14SurfaceStrip extends ICAOAnnex14Surface {

    @Id
    @SequenceGenerator(
            name = "ICAOAnnex14SurfaceStripGenerator",
            sequenceName = "seq_icao14_surface_strip",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ICAOAnnex14SurfaceStripGenerator"
    )
    @Column
    private Long id;

    @Column
    private Double length;

    @Column
    private Double width;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.STRIP;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.STRIP.description();
    }
}
