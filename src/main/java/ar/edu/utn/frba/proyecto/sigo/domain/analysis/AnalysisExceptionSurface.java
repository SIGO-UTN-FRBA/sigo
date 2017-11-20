package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import com.vividsolutions.jts.geom.Polygon;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "public.tbl_exception_surface_spec")
@PrimaryKeyJoinColumn(name = "exception_id")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class AnalysisExceptionSurface extends AnalysisException {

    @Builder
    public AnalysisExceptionSurface(
            Long id,
            String name,
            AnalysisExceptions type,
            AnalysisCase analysisCase,
            String surfaceName,
            Map<String, Double> properties
    ){
        super(id, name, type, analysisCase);
        this.surfaceName = surfaceName;
        this.properties = properties;
    }

    @Id
    @SequenceGenerator(name = "exceptionSurfaceGenerator", sequenceName = "EXCEPTION_SURFACE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exceptionSurfaceGenerator")
    @Column(name = "exception_surface_id")
    private Long id;

    @Column(name = "surface_name")
    private String surfaceName;

    @ElementCollection
    @MapKeyColumn(name = "property")
    @Column(name = "value")
    private Map<String, Double> properties =new HashMap<>();

    @Column(name = "geom")
    private Polygon geom;

    @Override
    public <T> T accept(AnalysisExceptionVisitor<T> visitor) {
        return visitor.visitAnalysisExceptionSurface(this);
    }
}