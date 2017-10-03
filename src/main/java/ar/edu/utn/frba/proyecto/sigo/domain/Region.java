package ar.edu.utn.frba.proyecto.sigo.domain;

import javax.persistence.*;
import java.util.List;
import lombok.*;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


@Entity
@Table(name = "public.tbl_regions")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class Region {
    @Id
    @SequenceGenerator(name = "regionGenerator", sequenceName = "REGION_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regionGenerator")
    @Column(name = "region_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code_fir" , length=4)
    private String codeFIR;

    @Column(name = "geom")
    private Polygon geom;

    @OneToMany(mappedBy="region", cascade = CascadeType.REMOVE)
    private List<Airport> airports;

}