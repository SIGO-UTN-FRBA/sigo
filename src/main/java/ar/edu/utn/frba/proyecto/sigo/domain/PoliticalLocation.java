package ar.edu.utn.frba.proyecto.sigo.domain;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "public.tbl_political_locations")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class PoliticalLocation {
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
    @JoinColumn(name = "parten_id")
    private PoliticalLocation politicallocation;

    @Column(name = "geom")
    private MultiPolygon geom;

    @OneToMany(mappedBy="politicallocation")
    private List<PoliticalLocation> politicallocations;

    @OneToMany(mappedBy="politicalocation")
    private List<PoliticalLocationType> politicallocationtypes;


}