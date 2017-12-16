package ar.edu.utn.frba.proyecto.sigo.domain.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Point;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.*;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "public.tbl_runway_directions")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class RunwayDirection extends SigoDomain implements Spatial<Point> {

    @Id
    @SequenceGenerator(name = "directionGenerator", sequenceName = "RUNWAY_DIRECTION_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "directionGenerator")
    @Column(name = "direction_id")
    private Long id;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "azimuth")
    private Double azimuth;

    @Enumerated(EnumType.ORDINAL)
    @Column(name= "position")
    private RunwayDirectionPositions position;

    @ManyToOne
    @JoinColumn(name="runway_id", nullable=false, updatable= false)
    private Runway runway;

    @Column(name = "geom")
    private Point geom;

    @OneToOne(mappedBy = "runwayDirection", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private RunwayTakeoffSection takeoffSection;

    @OneToOne(mappedBy = "runwayDirection", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private RunwayApproachSection approachSection;

    @OneToOne(mappedBy = "runwayDirection", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private RunwayClassification classification;

    @Column(name = "height")
    private Double height;

    @OneToOne
    @JoinColumn(name = "strip_id")
    private RunwayStrip strip;


    public String getIdentifier(){

        return String.format(
                "%s%s",
                StringUtils.leftPad(number.toString(), 2, "0"),
                Optional.ofNullable(position).map(RunwayDirectionPositions::position).get()
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
