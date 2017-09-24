package ar.edu.utn.frba.proyecto.sigo.domain;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;
import com.vividsolutions.jts.geom.Point;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@Entity
@Table(name = "public.tbl_aerodromes")
public class Airport {

    @Id
    @SequenceGenerator(name = "airportGenerator", sequenceName = "AIRPORT_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airportGenerator")
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

    @Override
    public String toString(){

        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .toString();
    }
}
