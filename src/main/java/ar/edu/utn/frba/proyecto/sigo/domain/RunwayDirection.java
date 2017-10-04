package ar.edu.utn.frba.proyecto.sigo.domain;

import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Point;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "public.tbl_runway_directions")
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

    @Column(name = "number", nullable = false)
    private Integer number;

    @Enumerated(EnumType.ORDINAL)
    @Column(name= "position")
    private RunwayDirectionPosition position;

    @ManyToOne
    @JoinColumn(name="runway_id", nullable=false, updatable= false)
    private Runway runway;

    @Column(name = "geom")
    private Point geom;
  
    @OneToOne
    @JoinColumn(name = "relation_id", foreignKey = @ForeignKey(name = "type_direction_fk"))
    private RunwayTypeImplICAOAnnex14 type;

    public String getIdentifier(){

        return String.format(
                "%s%s",
                StringUtils.leftPad(number.toString(), 2, "0"),
                Optional.ofNullable(position).map(RunwayDirectionPosition::position).get()
        );
    }

    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("number:", number)
                .add("position", position)
                .add("runway", Optional.ofNullable(runway).map(Runway::getId).orElse(null))
                .toString();
    }

}
