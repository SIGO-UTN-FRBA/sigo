package ar.edu.utn.frba.proyecto.sigo.domain;

import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Polygon;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;



@Entity
@Table(name = "public.tbl_runway_distances")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class RunwayDistance {
    @Id
    @SequenceGenerator(name = "runwayDistancesGenerator", sequenceName = "Runway_Distance_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayDistancesGenerator")

    @Column(name = "distance_id")
    private Long distance_id;

    @ManyToOne
    @JoinColumn(name = "type_id", foreignKey = @ForeignKey(name = "type_id_fk"))
    private RunwayDistanceType runwaydistancetype;

    @ManyToOne
    @JoinColumn(name="direction_id", nullable=false, updatable= false)
    private RunwayDirection runwaydirection;

    @Column(name = "length")
    private Long length;

    @Column(name = "geom")
    private Polygon geom;

}
