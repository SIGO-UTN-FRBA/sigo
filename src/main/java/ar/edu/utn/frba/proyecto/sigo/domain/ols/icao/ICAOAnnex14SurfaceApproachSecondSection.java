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
@Table(name = "tbl_icao14_surface_approach_second")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ICAOAnnex14SurfaceApproachSecondSection extends ICAOAnnex14Surface <Polygon>
{

    @Column
    private Double length;

    @Column
    private Double slope;

    @Column(name = "initial_height")
    private Double initialHeight;

    @Column(name="geom")
    private Polygon geometry;

    @Column(name = "limit_height")
    private Double limitHeight;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.APPROACH_SECOND_SECTION;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.APPROACH_SECOND_SECTION.description();
    }

    @Builder
    public ICAOAnnex14SurfaceApproachSecondSection(Long id, ICAOAnnex14RunwayClassifications classification, ICAOAnnex14RunwayCategories category, ICAOAnnex14RunwayCodeNumbers code, Polygon geometry, Double length, Double slope, Double limitHeight) {
        super(id, classification, category, code);
        this.length = length;
        this.slope = slope;
        this.limitHeight = limitHeight;
    }
}
