package ar.edu.utn.frba.proyecto.sigo.domain.object;

import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import com.vividsolutions.jts.geom.LineString;
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
public class PlacedObjectOverheadWire extends PlacedObject<LineString> {

    @Builder
    public PlacedObjectOverheadWire(Long id, String name, Double heightAgl, Double heightAmls, String subtype, Boolean verified, PoliticalLocation politicalLocation, PlacedObjectOwner owner, Boolean temporary, LightingTypes lighting, MarkIndicatorTypes markIndicator, LineString geom) {
        super(id, name, heightAgl, heightAmls, subtype, verified, politicalLocation, owner, temporary, lighting, markIndicator);
        this.geom = geom;
    }

    @Column(name = "geom")
    private LineString geom;

    @Override
    public ElevatedObjectTypes getType() {
        return ElevatedObjectTypes.OVERHEAD_WIRED;
    }

    @Override
    public <P> P accept(PlacedObjectVisitor<P> visitor) {
        return visitor.visitPlacedObjectOverheadWire(this);
    }

}