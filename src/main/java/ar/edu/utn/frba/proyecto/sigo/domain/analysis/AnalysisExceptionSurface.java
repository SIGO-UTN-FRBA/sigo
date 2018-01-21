package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalyst;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
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
public class AnalysisExceptionSurface extends AnalysisException implements AnalysisRestriction {

    @Builder
    public AnalysisExceptionSurface(
            Long id,
            String name,
            AnalysisCase analysisCase,
            Double heightAmls,
            Polygon geom
    ){
        super(id, name, analysisCase);
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

    @Override
    public Geometry getGeometry() {
        return this.getGeom();
    }

    @Override
    public Double determineHeightAt(Point point, OlsAnalyst analyst) {
        return this.getHeightAmls();
    }

    @Override
    public AnalysisRestrictionTypes getRestrictionType() {
        return AnalysisRestrictionTypes.TERRAIN_SURFACE_EXCEPTION;
    }

    @Override
    public AnalysisExceptionTypes getType() {
        return AnalysisExceptionTypes.SURFACE;
    }
}