package ar.edu.utn.frba.proyecto.sigo.utils.json;

import com.google.gson.*;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

public class PolygonSerializer implements JsonSerializer<Polygon> {

    @Override
    public JsonElement serialize(Polygon polygon, Type type, JsonSerializationContext jsonSerializationContext) {
        try {
            OutputStream out = new ByteArrayOutputStream();

            new GeometryJSON().writePolygon(polygon, out);

            return new JsonParser()
                    .parse(out.toString())
                    .getAsJsonObject();

        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }
}
