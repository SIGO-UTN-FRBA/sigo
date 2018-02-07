package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_icao14_surface_inner_transitional")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ICAOAnnex14SurfaceInnerTransitional extends ICAOAnnex14Surface<MultiPolygon>
{

    @Column
    private Double slope;

    @Column(name="geom")
    private MultiPolygon geometry;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.INNER_TRANSITIONAL;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.INNER_TRANSITIONAL.description();
    }

    @Builder
    public ICAOAnnex14SurfaceInnerTransitional(Long id, ICAOAnnex14RunwayClassifications classification, ICAOAnnex14RunwayCategories category, ICAOAnnex14RunwayCodeNumbers code, MultiPolygon geometry, Double slope) {
        super(id, classification, category, code);
        this.slope = slope;
        this.geometry = geometry;
    }
}
