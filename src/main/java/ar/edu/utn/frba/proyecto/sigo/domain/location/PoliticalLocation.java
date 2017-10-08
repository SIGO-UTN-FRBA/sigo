package ar.edu.utn.frba.proyecto.sigo.domain.location;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "public.tbl_political_locations")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class PoliticalLocation extends SigoDomain implements Spatial<MultiPolygon> {
    @Id
    @SequenceGenerator(name = "politicalLocationGenerator", sequenceName = "POLITICAL_LOCATION_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "politicalLocationGenerator")
    @Column(name = "location_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PoliticalLocation parent;

    @Column(name = "geom")
    private MultiPolygon geom;

    @OneToMany(mappedBy="parent")
    private List<PoliticalLocation> children;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PoliticalLocationType type;


}