package ar.edu.utn.frba.proyecto.sigo.domain.object;

import com.vividsolutions.jts.geom.Geometry;

public interface PlacedObjectSpec {

    Long getId();
    PlacedObject getPlacedObject();
    Geometry getGeom();
}
