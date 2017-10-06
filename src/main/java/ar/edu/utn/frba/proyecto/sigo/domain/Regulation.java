package ar.edu.utn.frba.proyecto.sigo.domain;

import javax.persistence.*;
import java.util.List;
import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;

@Entity
@Table(name = "public.tbl_regulations")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class Regulation {
    @Id
    @SequenceGenerator(name = "regulationGenerator", sequenceName = "REGULATION_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regulationGenerator")
    @Column(name = "regulation_id")
    private Long id;

    @Column(name = "authority")
    private String authority;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

/*TODO habilitar cuando este
    @OneToMany(mappedBy="regulation")
    private Set<AnalysCase> analysiscases;
*/
    /*TODO habilitar cuando este
    @OneToMany(mappedBy="regulation")
    private List<OLSRule> OLSRules;*/
}