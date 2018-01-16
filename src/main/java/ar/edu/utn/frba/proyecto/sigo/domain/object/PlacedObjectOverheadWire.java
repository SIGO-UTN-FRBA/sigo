package ar.edu.utn.frba.proyecto.sigo.domain.object;

import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import com.vividsolutions.jts.geom.MultiLineString;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "public.tbl_placed_object_overhead_wire")
@NoArgsConstructor
@Data
public class PlacedObjectOverheadWire extends PlacedObject<MultiLineString> {

    @Builder
    public PlacedObjectOverheadWire(
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
            MultiLineString geom
    ) {
        super(id, name, heightAgl, heightAmls, type, subtype, verified, politicalLocation, owner, temporary, lighting, markIndicator);
        this.geom = geom;
    }

    @Column(name = "geom")
    private MultiLineString geom;

    @Override
    public <P> P accept(PlacedObjectVisitor<P> visitor) {
        return visitor.visitPlacedObjectOverheadWire(this);
    }

}