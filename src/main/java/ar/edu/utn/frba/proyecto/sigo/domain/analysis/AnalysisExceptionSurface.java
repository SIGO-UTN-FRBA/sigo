package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import com.vividsolutions.jts.geom.Polygon;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
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
            Double heightAmls,
            Polygon geom
    ){
        super(id, name, type, analysisCase);
        this.heightAmls = heightAmls;
        this.geom = geom;
    }

    @Column(name = "height_amls")
    private Double heightAmls;

    @Column(name = "geom")
    private Polygon geom;

    @Override
    public <T> T accept(AnalysisExceptionVisitor<T> visitor) {
        return visitor.visitAnalysisExceptionSurface(this);
    }
}