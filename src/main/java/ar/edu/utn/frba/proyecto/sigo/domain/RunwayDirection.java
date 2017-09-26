package ar.edu.utn.frba.proyecto.sigo.domain;

import com.vividsolutions.jts.geom.Point;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "public.tbl_runway_dimensions")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class RunwayDirection extends SigoDomain implements Spatial<Point>{

    @Id
    @SequenceGenerator(name = "directionGenerator", sequenceName = "RUNWAY_DIRECTION_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "directionGenerator")
    @Column(name = "direction_id")
    private Long id;

    @Column(name = "code")
    private Integer code;

    @Column(name = "available")
    private Boolean available;


    @ManyToOne
    @JoinColumn(name="runway_id", nullable=false, updatable= false)
    private Runway runway;

    @Column(name = "geom")
    private Point geom;
}
