package ar.edu.utn.frba.proyecto.sigo.domain.location.political;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import com.google.common.base.MoreObjects;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", updatable = false)
    private PoliticalLocation parent;

    @Column(name = "geom")
    private MultiPolygon geom;

    @OneToMany(mappedBy="parent", cascade = CascadeType.REMOVE)
    private List<PoliticalLocation> children;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PoliticalLocationType type;


    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name:", name)
                .add("code", code)
                .toString();
    }
}