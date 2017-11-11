package ar.edu.utn.frba.proyecto.sigo.domain.object;

import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "public.tbl_placed_object_individual")
@PrimaryKeyJoinColumn(name = "object_id")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class PlacedObjectIndividual extends PlacedObject{

    @Builder
    public PlacedObjectIndividual(
            Long id,
            String name,
            PlacedObjectTypes type,
            String subtype,
            Boolean verified,
            PoliticalLocation politicalLocation,
            PlacedObjectOwner owner,
            Double heightAgl,
            Double heightAmls,
            Boolean temporary,
            LightingTypes lighting,
            MarkIndicatorTypes markIndicator,
            Point geom
    ) {
        super(id, name, type, subtype, verified, politicalLocation, owner, heightAgl, heightAmls, temporary, lighting, markIndicator);
        this.geom = geom;
    }

    @Column(name = "geom")
    private Geometry geom;

    @Override
    public Class getGeomClass() {
        return Point.class;
    }

    @Override
    public <T> T accept(PlacedObjectVisitor<T> visitor) {
        return visitor.visitPlacedObjectIndividual(this);
    }
}
