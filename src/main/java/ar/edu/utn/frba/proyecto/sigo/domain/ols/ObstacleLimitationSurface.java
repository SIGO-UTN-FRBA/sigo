package ar.edu.utn.frba.proyecto.sigo.domain.ols;

import com.vividsolutions.jts.geom.Geometry;

public interface ObstacleLimitationSurface {

    String getName();
    Long getId();
    <T extends Geometry> T getGeometry();
}
