package ar.edu.utn.frba.proyecto.sigo.domain.regulation;

import javax.persistence.*;
import java.util.List;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;

@Entity
@Table(name = "public.tbl_regulations")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class Regulation extends SigoDomain {
    @Id
    @SequenceGenerator(name = "regulationGenerator", sequenceName = "REGULATION_SEQUENCE", allocationSize = 1)
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