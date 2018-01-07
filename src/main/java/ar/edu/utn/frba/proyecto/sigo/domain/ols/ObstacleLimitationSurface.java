package ar.edu.utn.frba.proyecto.sigo.domain.ols;

import com.vividsolutions.jts.geom.Geometry;

public interface ObstacleLimitationSurface <T extends Geometry>{

    String getName();
    Long getId();
    T getGeometry();
    void setGeometry(T geometry);
}
