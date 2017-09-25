package ar.edu.utn.frba.proyecto.sigo.commons.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;
import java.lang.reflect.Type;

public class PointDeserializer implements JsonDeserializer<Point> {

    @Override
    public Point deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return new GeometryJSON().readPoint(jsonElement.toString());

        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }
}
