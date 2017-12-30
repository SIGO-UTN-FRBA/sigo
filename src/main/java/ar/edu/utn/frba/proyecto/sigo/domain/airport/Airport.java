package ar.edu.utn.frba.proyecto.sigo.domain.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Point;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = "runways")
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@Table(name = "public.tbl_aerodromes")
public class Airport extends SigoDomain implements Spatial<Point> {

    @Id
    @SequenceGenerator(name = "airportGenerator", sequenceName = "AIRPORT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airportGenerator")
    @Column(name = "aerodrome_id")
    private Long id;

    @Column(name = "name_fir", nullable = false)
    private String nameFIR;

    @Column(name = "code_fir", length = 4)
    private String codeFIR;

    @Column(name = "code_iata", length = 3)
    private String codeIATA;

    @Column(name = "code_local", length = 3)
    private String codeLocal;

    @Column(name = "geom")
    private Point geom;

    @Column(name = "height")
    private Double height;


    @OneToMany(mappedBy = "airport", cascade = CascadeType.REMOVE)
    private List<Runway> runways;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="region_id", nullable=false, updatable= false)
    private Region region;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="regulation_id", nullable = false)
    private Regulations regulation;

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
