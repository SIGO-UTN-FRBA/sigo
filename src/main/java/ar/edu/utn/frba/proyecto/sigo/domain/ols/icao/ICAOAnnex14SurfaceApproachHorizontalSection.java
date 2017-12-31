package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
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
@Table(name = "tbl_icao14_surface_approach_horizontal")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ICAOAnnex14SurfaceApproachHorizontalSection extends ICAOAnnex14Surface {

    @Column
    private Double length;

    @Column(name = "total_length")
    private Double totalLength;

    @Override
    public ICAOAnnex14Surfaces getEnum() {
        return ICAOAnnex14Surfaces.APPROACH_HORIZONTAL_SECTION;
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.APPROACH_HORIZONTAL_SECTION.description();
    }

    @Builder
    public ICAOAnnex14SurfaceApproachHorizontalSection(Long id, ICAOAnnex14RunwayClassifications classification, ICAOAnnex14RunwayCategories category, ICAOAnnex14RunwayCodeNumbers code, Polygon geometry, Double length, Double totalLength) {
        super(id, classification, category, code, geometry);
        this.length = length;
        this.totalLength = totalLength;
    }
}
