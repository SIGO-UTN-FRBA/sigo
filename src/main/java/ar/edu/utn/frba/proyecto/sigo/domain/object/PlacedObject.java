package ar.edu.utn.frba.proyecto.sigo.domain.object;

import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.location.political.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.domain.location.geographic.Region;
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


    public Long getSpecId() {
        if(this.getIndividualSpec() != null)
            return this.getIndividualSpec().getId();
        else if (this.getBuildingSpec() != null)
            return this.getBuildingSpec().getId();
        else if (this.getWireSpec() != null)
            return this.wireSpec.getId();
        else
            return null;
    }
}
