package ar.edu.utn.frba.proyecto.sigo.airport;

import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Entity
@Table(name = "public.tbl_aerodromes")
public class Airport {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "aerodrome_id") private Long id;
    @Column(name = "name_fir") private String nameFIR;
    @Column(name = "code_fir") private String codeFIR;
    @Column(name = "code_iata") private String codeIATA;
    @Column(name = "geom") private Point geom;
}
