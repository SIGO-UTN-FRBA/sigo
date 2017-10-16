package ar.edu.utn.frba.proyecto.sigo.domain.object;

import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import com.vividsolutions.jts.geom.Geometry;

public interface PlacedObjectSpec<T extends Geometry> extends Spatial<T> {

    Long getId();
    PlacedObject getPlacedObject();
    T getGeom();
    Class getGeomClass();
}
