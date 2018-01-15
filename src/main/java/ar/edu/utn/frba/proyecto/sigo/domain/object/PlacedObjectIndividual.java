package ar.edu.utn.frba.proyecto.sigo.domain.object;

import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import com.vividsolutions.jts.geom.Point;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "public.tbl_placed_object_individual")
@Data
public class PlacedObjectIndividual extends PlacedObject<Point> {

    @Builder
    public PlacedObjectIndividual(
            Long id,
            String name,
            ElevatedObjectTypes type,
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
        super(id, name, heightAgl, heightAmls, geom, type, subtype, verified, politicalLocation, owner, temporary, lighting, markIndicator);
    }

    @Override
    public <P> P accept(PlacedObjectVisitor<P> visitor) {
        return visitor.visitPlacedObjectIndividual(this);
    }
}
