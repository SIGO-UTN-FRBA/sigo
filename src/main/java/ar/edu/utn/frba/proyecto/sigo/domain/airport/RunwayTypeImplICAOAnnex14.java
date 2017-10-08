package ar.edu.utn.frba.proyecto.sigo.domain.airport;


import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.*;

@Entity
@Table(name = "public.tbl_runway_type_impl_ICAOAnnex14")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class RunwayTypeImplICAOAnnex14 extends SigoDomain {
    @Id
    @SequenceGenerator(name = "runwayTypeImplICAOAnnex14Generator", sequenceName = "RunwayTypeImplICAOAnnex14_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayTypeImplICAOAnnex14Generator")
    @Column(name = "relation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "letter_id",foreignKey = @ForeignKey(name = "type_letter_fk"))
    private RunwayTypeNumberICAOAnnex14 number;

    @ManyToOne
    @JoinColumn(name = "number_id",foreignKey = @ForeignKey(name = "type_number_fk"))
    private RunwayTypeLetterICAOAnnex14 letter;
}