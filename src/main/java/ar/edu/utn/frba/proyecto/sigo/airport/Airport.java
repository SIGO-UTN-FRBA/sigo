package ar.edu.utn.frba.proyecto.sigo.airport;

import com.vividsolutions.jts.geom.Point;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@Entity
@Table(name = "public.tbl_aerodromes")
public class Airport implements Serializable{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "aerodrome_id")
    private Long id;

    @Column(name = "name_fir", nullable = false)
    private String nameFIR;

    @Column(name = "code_fir", nullable = false, unique = true)
    private String codeFIR;

    @Column(name = "code_iata")
    private String codeIATA;

    @Column(name = "geom")
    private Point geom;
}
