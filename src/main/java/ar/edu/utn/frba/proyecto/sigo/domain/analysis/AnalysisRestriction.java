package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalyst;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public interface AnalysisRestriction {

    Long getId();
    AnalysisRestrictionTypes getRestrictionType();
    String getName();
    Geometry getGeometry();

    Double determineHeightAt(Point point, OlsAnalyst analyst);
}
