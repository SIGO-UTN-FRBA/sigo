package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
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
@Table(name = "tbl_icao14_surface_conical")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ICAOAnnex14SurfaceConical extends ICAOAnnex14Surface {

    @Column
    private Double slope;

    @Column(name = "final_height")
    private Double finalHeight;

    @Column(name = "initial_height")
    private Double initialHeight;

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

    @Builder
    public ICAOAnnex14SurfaceConical(Long id, ICAOAnnex14RunwayClassifications classification, ICAOAnnex14RunwayCategories category, ICAOAnnex14RunwayCodeNumbers code, Polygon geometry, Double slope, Double finalHeight, Double ratio) {
        super(id, classification, category, code, geometry);
        this.slope = slope;
        this.finalHeight = finalHeight;
        this.ratio = ratio;
    }
}
