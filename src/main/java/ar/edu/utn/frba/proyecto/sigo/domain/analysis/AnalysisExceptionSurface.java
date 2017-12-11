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
@Table(name = "public.tbl_analysis_exceptions_surface")
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
            Double heightAgl
    ){
        super(id, name, type, analysisCase);
        this.heightAgl = heightAgl;
    }

    @Column(name = "height_AGL")
    private Double heightAgl;

    @Column(name = "geom")
    private Polygon geom;

    @Override
    public <T> T accept(AnalysisExceptionVisitor<T> visitor) {
        return visitor.visitAnalysisExceptionSurface(this);
    }
}