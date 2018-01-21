package ar.edu.utn.frba.proyecto.sigo.domain.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import com.vividsolutions.jts.geom.Geometry;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ObstacleLimitationSurface <T extends Geometry>{

    public abstract String getName();
    public abstract Long getId();
    public abstract T getGeometry();
    public abstract void setGeometry(T geometry);
    public abstract Regulations getRegulation();
}
