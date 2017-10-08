package ar.edu.utn.frba.proyecto.sigo.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import com.vividsolutions.jts.geom.Polygon;

@Entity
@Table(name = "public.tbl_exception_surface_spec")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ExceptionSurfaceSpec {
    @Id
    @SequenceGenerator(name = "exceptionSurfaceSpecGenerator", sequenceName = "EXCEPTION_SURFACE_SPEC_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exceptionSurfaceSpecGenerator")
    @Column(name = "spec_id")
    private Long id;

    @Column(name = "surface_name")
    private String surfaceName;

    @Column(name = "properties")
    private String properties;

    @Column(name = "geom")
    private Polygon geom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exception_id")
    private AnalysisException exception;

}