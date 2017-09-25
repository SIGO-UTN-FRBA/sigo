package ar.edu.utn.frba.proyecto.sigo.domain;

import com.vividsolutions.jts.geom.Geometry;

public interface Spatial<T extends Geometry> {

    void setGeom(T geom);

    T getGeom();
}
