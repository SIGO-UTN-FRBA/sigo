package ar.edu.utn.frba.proyecto.sigo.domain.location;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "public.tbl_political_location_types")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class PoliticalLocationType extends SigoDomain {
    @Id
    @SequenceGenerator(name = "PoliticalLocationTypeGenerator", sequenceName = "POLITICA_LOCATION_TYPE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PoliticalLocationTypeGenerator")
    @Column(name = "type_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "index")
    private Long index;


    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name:", name)
                .add("index", index)
                .toString();
    }
}