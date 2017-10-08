package ar.edu.utn.frba.proyecto.sigo.domain.airport;


import javax.persistence.*;
import java.util.List;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.*;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.MultiPolygon;

@Entity
@Table(name = "public.tbl_runway_classification_ICAOAnnex14")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class RunwayClassificationICAOAnnex14 extends SigoDomain {
    @Id
    @SequenceGenerator(name = "runwayClassificationICAOAnnex14Generator", sequenceName = "RunwayClassificationICAOAnnex14_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayClassificationICAOAnnex14Generator")

    @Column(name = "classification_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

/*TODO    @OneToMany(mappedBy="runwayclassificationICAOAnnex14")
    private Set<RunwayClassificationImplICAOAnnex14> runwayclassificationimplICAOAnnex14s;
*/
}