package ar.edu.utn.frba.proyecto.sigo.domain;

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
}
