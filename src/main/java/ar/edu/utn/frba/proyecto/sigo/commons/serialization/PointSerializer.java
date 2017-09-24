package ar.edu.utn.frba.proyecto.sigo.commons.serialization;

import com.google.gson.*;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;
import java.lang.reflect.Type;

public class PointSerializer implements JsonSerializer<Point> {
    @Override
    public JsonElement serialize(Point point, Type type, JsonSerializationContext jsonSerializationContext) {

        return new JsonObject();
    }
}


