package ar.edu.utn.frba.proyecto.sigo.domain.airport;


import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.*;

@Entity
@Table(name = "public.tbl_runway_classification_impl_ICAOAnnex14")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class RunwayClassificationImplICAOAnnex14 extends SigoDomain {
    @Id
    @SequenceGenerator(name = "runwayClassificationImplICAOAnnex14Generator", sequenceName = "RunwayClassificationImplICAOAnnex14_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayClassificationImplICAOAnnex14Generator")
    @Column(name = "relation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "classification_id", foreignKey = @ForeignKey(name = "classification_id_fk"))
    private RunwayClassificationICAOAnnex14 value;

}