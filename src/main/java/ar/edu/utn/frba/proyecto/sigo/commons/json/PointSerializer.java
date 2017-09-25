package ar.edu.utn.frba.proyecto.sigo.commons.json;

import com.google.gson.*;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

public class PointSerializer implements JsonSerializer<Point> {
    @Override
    public JsonElement serialize(Point point, Type type, JsonSerializationContext jsonSerializationContext) {

        try {
            OutputStream out = new ByteArrayOutputStream();

            new GeometryJSON().writePoint(point, out);

            return new JsonParser()
                    .parse(out.toString())
                    .getAsJsonObject();

        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }
}


