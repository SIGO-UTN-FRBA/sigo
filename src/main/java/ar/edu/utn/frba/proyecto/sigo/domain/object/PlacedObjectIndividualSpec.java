package ar.edu.utn.frba.proyecto.sigo.domain.object;

import javax.persistence.*;

import lombok.*;
import com.vividsolutions.jts.geom.Point;

@Entity
@Table(name = "public.tbl_placed_object_individual_spec")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class PlacedObjectIndividualSpec implements PlacedObjectSpec<Point> {
    @Id
    @SequenceGenerator(name = "placedObjectIndividualGenerator", sequenceName = "PLACED_OBJECT_INDIVIDUAL_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "placedObjectIndividualGenerator")
    @Column(name = "spec_id")
    private Long id;

    @Column(name = "geom")
    private Point geom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id")
    private PlacedObject placedObject;


    @Override
    public Class getGeomClass() {
        return Point.class;
    }
}
