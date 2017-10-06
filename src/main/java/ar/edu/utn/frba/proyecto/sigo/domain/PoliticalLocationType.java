package ar.edu.utn.frba.proyecto.sigo.domain;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "public.tbl_political_location_type")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class PoliticalLocationType {
    @Id
    @SequenceGenerator(name = "PoliticalLocationTypeGenerator", sequenceName = "POLITICA_LOCATION_TYPE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PoliticalLocationTypeGenerator")
    @Column(name = "type_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "name")
    private PoliticalLocation politicalocation;

    @Column(name = "index")
    private Long index;

}