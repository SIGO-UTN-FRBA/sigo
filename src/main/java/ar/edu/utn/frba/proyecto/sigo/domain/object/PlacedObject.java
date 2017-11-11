package ar.edu.utn.frba.proyecto.sigo.domain.object;

import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Geometry;
import lombok.*;

@Entity
@Table(name = "public.tbl_placed_object")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public abstract class PlacedObject extends SigoDomain implements Spatial<Geometry>{
    @Id
    @SequenceGenerator(name = "placedObjectGenerator", sequenceName = "PLACED_OBJECT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "placedObjectGenerator")
    @Column(name = "object_id")
    protected Long id;

    @Column(name = "name")
    protected String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    protected PlacedObjectTypes type;

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

    @Column(name = "height_agl")
    protected Double heightAgl;

    @Column(name = "height_amls")
    protected Double heightAmls;

    @Column(name = "temporary")
    protected Boolean temporary;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "lighting")
    protected LightingTypes lighting;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "mark_indicator")
    protected MarkIndicatorTypes markIndicator;


    public abstract Class getGeomClass();


    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type.name())
                .add("name:", name)
                .toString();
    }

    public abstract <T> T accept(PlacedObjectVisitor<T> visitor);
}
