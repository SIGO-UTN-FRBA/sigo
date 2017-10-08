package ar.edu.utn.frba.proyecto.sigo.domain;

import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Point;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@Table(name = "public.tbl_aerodromes")
public class Airport extends SigoDomain implements Spatial<Point> {

    @Id
    @SequenceGenerator(name = "airportGenerator", sequenceName = "AIRPORT_SEQUENCE", allocationSize = 1,initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airportGenerator")
    @Column(name = "aerodrome_id")
    private Long id;

    @Column(name = "name_fir", nullable = false)
    private String nameFIR;

    @Column(name = "code_fir", nullable = false, unique = true, length = 4)
    private String codeFIR;

    @Column(name = "code_iata", length = 3)
    private String codeIATA;

    @Column(name = "geom")
    private Point geom;

    @OneToMany(mappedBy = "airport", cascade = CascadeType.REMOVE)
    private List<Runway> runways;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="region_id", nullable=false, updatable= false)
    private Region region;

    @ManyToOne
    @JoinColumn(name="regulation_id", nullable = false, updatable = true)
    private Regulation regulation;

    public String toString(){

        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("codeFIR", codeFIR)
                .add("nameFIR", nameFIR)
                .toString();
    }

    public void addRunway(Runway runway) {
        this.runways.add(runway);
    }
}
