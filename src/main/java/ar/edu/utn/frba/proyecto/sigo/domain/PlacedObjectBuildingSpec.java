package ar.edu.utn.frba.proyecto.sigo.domain;


import javax.persistence.*;
import java.util.List;
import lombok.*;
import com.vividsolutions.jts.geom.MultiPolygon;

@Entity
@Table(name = "public.tbl_placed_object_building_spec")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class PlacedObjectBuildingSpec {
    @Id
    @SequenceGenerator(name = "placedObjectBuildingSpecGenerator", sequenceName = "PLACED_OBJECT_BUILDING_SPEC_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "placedObjectBuildingSpecGenerator")
    @Column(name = "spec_id")
    private Long id;

    @Column(name = "geom")
    private MultiPolygon geom;
/*
    @OneToMany(mappedBy="placedobjectbuildingspec")
    private List<PlacedObject> placedobjects;*/

}