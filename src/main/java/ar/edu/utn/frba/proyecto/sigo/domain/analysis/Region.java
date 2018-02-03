package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = "airports")
@Entity
@Table(name = "public.tbl_regions")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class Region extends SigoDomain<Long> implements Spatial<MultiPolygon> {
    @Id
    @SequenceGenerator(
            name = "regionGenerator",
            sequenceName = "REGION_SEQUENCE",
            allocationSize = 1,
            initialValue = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "regionGenerator"
    )
    @Column(name = "region_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code_fir" , length=4)
    private String codeFIR;

    @Column(name = "geom")
    private MultiPolygon geom;

    @OneToMany(mappedBy="region", cascade = CascadeType.REMOVE)
    private List<Airport> airports;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="state_id", nullable=false, updatable= false)
    private State state;


    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name:", name)
                .add("code_fir", codeFIR)
                .toString();
    }
}