package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import com.vividsolutions.jts.geom.Polygon;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_icao14_surface_conical")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ICAOAnnex14SurfaceConical extends ICAOAnnex14Surface<Polygon>
{

    @Column
    private Double slope;

    @Column(name = "final_height")
    private Double finalHeight;

    @Column(name = "initial_height")
    private Double initialHeight;

    @Column
    private Double ratio;

    @Column(name="geom")
    private Polygon geometry;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.CONICAL;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.CONICAL.description();
    }

    @Builder
    public ICAOAnnex14SurfaceConical(Long id, ICAOAnnex14RunwayClassifications classification, ICAOAnnex14RunwayCategories category, ICAOAnnex14RunwayCodeNumbers code, Polygon geometry, Double slope, Double finalHeight, Double ratio) {
        super(id, classification, category, code);
        this.slope = slope;
        this.finalHeight = finalHeight;
        this.ratio = ratio;
        this.geometry = geometry;
    }
}
