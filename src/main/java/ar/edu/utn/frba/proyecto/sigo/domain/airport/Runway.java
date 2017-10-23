package ar.edu.utn.frba.proyecto.sigo.domain.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Polygon;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "public.tbl_runway")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class Runway extends SigoDomain implements Spatial<Polygon> {

    @Id
    @SequenceGenerator(name = "runwayGenerator", sequenceName = "RUNWAY_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayGenerator")
    @Column(name = "runway_id")
    private Long id;

    @Column(name = "width")
    private Double width;

    @Column(name = "length")
    private Double length;

    @Column(name = "geom")
    private Polygon geom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="airport_id", nullable=false, updatable= false)
    private Airport airport;

    @OneToMany(mappedBy = "runway", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("number ASC")
    private List<RunwayDirection> directions;

    @ManyToOne
    @JoinColumn(name = "surface_id", foreignKey = @ForeignKey(name = "surface_id_fk"))
    private RunwaySurface surface;

    public String getName(){

        String directionIdentifiers = this.getDirections()
                .stream()
                .map(RunwayDirection::getIdentifier)
                .collect(Collectors.joining("/"));

        if(directionIdentifiers.isEmpty())
            directionIdentifiers = "XX/XX";

        return String.format("RNW %s", directionIdentifiers);
    }

    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("airport", Optional.ofNullable(airport).map(Airport::getId).orElse(null))
                .toString();
    }
}
