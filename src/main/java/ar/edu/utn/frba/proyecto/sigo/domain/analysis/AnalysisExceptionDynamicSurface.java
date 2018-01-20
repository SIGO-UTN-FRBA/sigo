package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import com.vividsolutions.jts.geom.Polygon;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "public.tbl_analysis_exceptions_dynamic_surface")
@PrimaryKeyJoinColumn(name = "exception_id")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class AnalysisExceptionDynamicSurface extends AnalysisException {

    @Column(name = "geom")
    private Polygon geom;

    @Column(name = "function")
    private String function  = "";

    @Builder
    public AnalysisExceptionDynamicSurface(
            Long id,
            String name,
            AnalysisExceptions type,
            AnalysisCase analysisCase,
            String function,
            Polygon geom
    ){
        super(id, name, type, analysisCase);
        this.function = function;
        this.geom = geom;
    }

    @Override
    public <T> T accept(AnalysisExceptionVisitor<T> visitor) {
        return null;
    }
}
