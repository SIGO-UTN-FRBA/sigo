package ar.edu.utn.frba.proyecto.sigo.domain.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import com.google.common.base.MoreObjects;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "public.tbl_runway_surfaces")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class RunwaySurface extends SigoDomain {

    @Id
    @SequenceGenerator(name = "surfaceGenerator", sequenceName = "RUNWAY_SURFACE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "surfaceGenerator")
    @Column(name = "surface_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code", length = 3)
    private String code;


    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("code", code)
                .toString();
    }
}
