package ar.edu.utn.frba.proyecto.sigo.domain;


import javax.persistence.*;
import java.util.List;
import lombok.*;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.MultiPolygon;

/*RUNWAY*/
@Entity
@Table(name = "public.tbl_runway_type_letter_ICAOAnnex14")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class RunwayTypeLetterICAOAnnex14 extends SigoDomain {
    @Id
    @SequenceGenerator(name = "runwayTypeLetterICAOAnnex14Generator", sequenceName = "RunwayTypeLetterICAOAnnex14_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayTypeLetterICAOAnnex14Generator")
    @Column(name = "code_id")
    private Long id;

    @Column(name = "value",length = 1)
    private String value;

    @Column(name = "wingspan_min")
    private Double wingspanMin;

    @Column(name = "wingspan_max")
    private Double wingspanMax;

    @Column(name = "gear_wheel_span_min")
    private Double gearWheelSpanMin;

    @Column(name = "gear_wheel_span_max")
    private Double gearWheelSpanMax;
}