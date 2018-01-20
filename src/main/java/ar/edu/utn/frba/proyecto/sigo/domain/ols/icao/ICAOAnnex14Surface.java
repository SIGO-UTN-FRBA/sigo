package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import com.vividsolutions.jts.geom.Geometry;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_icao14_surfaces")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public abstract class ICAOAnnex14Surface<T extends Geometry> extends ObstacleLimitationSurface<T> {

    @Id
    @SequenceGenerator(
            name = "ICAOAnnex14SurfaceGenerator",
            sequenceName = "seq_icao14_surface",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ICAOAnnex14SurfaceGenerator"
    )
    @Column
    private Long id;

    @Transient
    public abstract ICAOAnnex14Surfaces getEnum();

    @Column
    private ICAOAnnex14RunwayClassifications classification;

    @Column
    private ICAOAnnex14RunwayCategories category;

    @Column
    private ICAOAnnex14RunwayCodeNumbers code;

    @Override
    public Regulations getRegulation() {
        return Regulations.ICAO_ANNEX_14;
    }
}
