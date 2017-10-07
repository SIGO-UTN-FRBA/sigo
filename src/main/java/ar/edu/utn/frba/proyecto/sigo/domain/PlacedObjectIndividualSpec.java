package ar.edu.utn.frba.proyecto.sigo.domain;

import javax.persistence.*;
import java.util.List;
import lombok.*;
import com.vividsolutions.jts.geom.Point;

@Entity
@Table(name = "public.tbl_placed_object_individual_spec")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class PlacedObjectIndividualSpec {
    @Id
    @SequenceGenerator(name = "placedObjectIndividualGenerator", sequenceName = "PLACED_OBJECT_INDIVIDUAL_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "placedObjectIndividualGenerator")
    @Column(name = "spec_id")
    private Long id;

    @Column(name = "geom")
    private Point geom;

/*    @OneToMany(mappedBy="placedobjectindividualspec")
    private List<PlacedObject> placedobjects;*/

}
