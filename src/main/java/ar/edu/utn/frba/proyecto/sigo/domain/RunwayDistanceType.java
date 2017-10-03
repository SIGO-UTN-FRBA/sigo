package ar.edu.utn.frba.proyecto.sigo.domain;

import javax.persistence.*;
import java.util.List;
import lombok.*;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.MultiPolygon;

@Entity
@Table(name = "public.tbl_runway_distances_types")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder

public class RunwayDistanceType {
    @Id
    @SequenceGenerator(name = "runwayDistanceTypeGenerator", sequenceName = "Runway_Distance_Type_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayDistanceTypeGenerator")
    @Column(name = "type_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code", length = 4)
    private String code;

    @Column(name = "description")
    private String description;

 /*   @OneToMany(mappedBy="runwaydistancetype")
    private Set<RunwayDistance> runwaydistances;

    @OneToMany(mappedBy="runwaydistancetype")
    private Set<RunwayDirection> runwaydirections;*/

}
