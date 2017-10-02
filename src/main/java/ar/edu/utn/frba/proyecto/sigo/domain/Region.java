package ar.edu.utn.frba.proyecto.sigo.domain;

import javax.persistence.*;
import java.util.List;
import lombok.*;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.MultiPolygon;


@Entity
@Table(name = "public.tbl_regions")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class Region {
    @Id
    @SequenceGenerator(name = "regionGenerator", sequenceName = "REGION_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regionGenerator")
    @Column(name = "region_id")
    private Long region_id;

    @Column(name = "name")
    private String name;

    @Column(name = "code_fir" , length=4)
    private String code_fir;

    @Column(name = "geom")
    private MultiPolygon geom;

    @OneToMany(mappedBy="region", cascade = CascadeType.REMOVE)
    private List<Airport> aiports;

}