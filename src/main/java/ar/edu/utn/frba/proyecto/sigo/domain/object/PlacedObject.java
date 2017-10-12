package ar.edu.utn.frba.proyecto.sigo.domain.object;

import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.location.political.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.domain.location.geographic.Region;
import lombok.*;

@Entity
@Table(name = "public.tbl_placed_object")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class PlacedObject {
    @Id
    @SequenceGenerator(name = "placedObjectGenerator", sequenceName = "PLACED_OBJECT_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "placedObjectGenerator")
    @Column(name = "object_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Enum type;

    @Column(name = "subtype")
    private String subtype;

    @Column(name = "verified")
    private Boolean verified;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private PoliticalLocation politicalLocation;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private PlacedObjectOwner placedObjectOwner;

    @Column(name = "height_agl")
    private Long heightAgl;

    @Column(name = "height_amls")
    private Long heightAmls;

    @Column(name = "temporary")
    private Boolean temporary;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "lighting")
    private LightingTypes lighting;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "mark_indicator")
    private MarkIndicatorTypes markIndicator;

    @OneToOne
    @JoinColumn(name = "type_individual_id")
    private PlacedObjectIndividualSpec placedObjectIndividualSpec;

    @OneToOne
    @JoinColumn(name = "type_building_id")
    private PlacedObjectBuildingSpec placedObjectBuildingSpec;

    @OneToOne
    @JoinColumn(name = "type_overhead_wire_id")
    private PlacedObjectOverheadWireSpec placedObjectOverheadWireSpec;

}
