package ar.edu.utn.frba.proyecto.sigo.domain.object;

import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public abstract class PlacedObject<T extends Geometry> extends ElevatedObject<T> {


    public PlacedObject(Long id, String name, Double heightAgl, Double heightAmls, ElevatedObjectTypes type, String subtype, Boolean verified, PoliticalLocation politicalLocation, PlacedObjectOwner owner, Boolean temporary, LightingTypes lighting, MarkIndicatorTypes markIndicator) {

        super(id, name, heightAgl, heightAmls, type);

        this.subtype = subtype;
        this.verified = verified;
        this.politicalLocation = politicalLocation;
        this.owner = owner;
        this.temporary = temporary;
        this.lighting = lighting;
        this.markIndicator = markIndicator;
    }

    @Column(name = "subtype")
    protected String subtype;

    @Column(name = "verified")
    protected Boolean verified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable=false, updatable= false)
    protected PoliticalLocation politicalLocation;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    protected PlacedObjectOwner owner;

    @Column(name = "temporary")
    protected Boolean temporary;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "lighting")
    protected LightingTypes lighting;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "mark_indicator")
    protected MarkIndicatorTypes markIndicator;

    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type.name())
                .add("name:", name)
                .toString();
    }

    public abstract <P> P accept(PlacedObjectVisitor<P> visitor);
}
