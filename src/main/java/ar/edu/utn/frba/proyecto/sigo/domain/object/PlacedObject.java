package ar.edu.utn.frba.proyecto.sigo.domain.object;

import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.location.political.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.domain.location.geographic.Region;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import com.google.common.base.MoreObjects;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Entity
@Table(name = "public.tbl_placed_object")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class PlacedObject extends SigoDomain {
    @Id
    @SequenceGenerator(name = "placedObjectGenerator", sequenceName = "PLACED_OBJECT_SEQUENCE")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable=false, updatable= false)
    private Region region;

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


    @OneToOne(
            mappedBy = "placedObject",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @LazyToOne( LazyToOneOption.NO_PROXY )
    private PlacedObjectIndividualSpec individualSpec;

    @OneToOne(
            mappedBy = "placedObject",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @LazyToOne( LazyToOneOption.NO_PROXY )
    private PlacedObjectBuildingSpec buildingSpec;

    @OneToOne(
            mappedBy = "placedObject",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @LazyToOne( LazyToOneOption.NO_PROXY )
    private PlacedObjectOverheadWireSpec wireSpec;



    public PlacedObjectSpec getSpecification(){
        switch (this.type) {
            case BUILDING:
                return this.getBuildingSpec();
            case OVERHEAD_WIRED:
                return this.getWireSpec();
            case INDIVIDUAL:
                return this.getIndividualSpec();
        }

        throw new SigoException("Missing PlacedObject type definition");
    }

    public void setSpecification(PlacedObjectSpec spec){
        switch (this.getType()) {
            case BUILDING:
                this.setBuildingSpec((PlacedObjectBuildingSpec)spec);
                break;
            case INDIVIDUAL:
                this.setIndividualSpec((PlacedObjectIndividualSpec)spec);
                break;
            case OVERHEAD_WIRED:
                this.setWireSpec((PlacedObjectOverheadWireSpec)spec);
                break;
        }
    }

    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type.name())
                .add("name:", name)
                .toString();
    }
}
