package ar.edu.utn.frba.proyecto.sigo.domain.object;

import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Geometry;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Entity
@Table(name = "public.tbl_placed_object")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public abstract class PlacedObject<T extends Geometry>
        extends SigoDomain
        implements Spatial<T>
{
    @Id
    @SequenceGenerator(name = "placedObjectGenerator", sequenceName = "PLACED_OBJECT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "placedObjectGenerator")
    @Column(name = "object_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private PlacedObjectTypes type;

    @Column(name = "subtype")
    private String subtype;

    @Column(name = "verified")
    private Boolean verified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable=false, updatable= false)
    private PoliticalLocation politicalLocation;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private PlacedObjectOwner owner;

    @Column(name = "height_agl")
    private Double heightAgl;

    @Column(name = "height_amls")
    private Double heightAmls;

    @Column(name = "temporary")
    private Boolean temporary;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "lighting")
    private LightingTypes lighting;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "mark_indicator")
    private MarkIndicatorTypes markIndicator;


    public abstract Class getGeomClass();

    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type.name())
                .add("name:", name)
                .toString();
    }
}
