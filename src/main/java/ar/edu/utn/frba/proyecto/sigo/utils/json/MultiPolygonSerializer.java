package ar.edu.utn.frba.proyecto.sigo.utils.json;

import com.google.gson.*;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

public class MultiPolygonSerializer implements JsonSerializer<MultiPolygon> {

    @Override
    public JsonElement serialize(MultiPolygon multiPolygon, Type type, JsonSerializationContext jsonSerializationContext) {
        try {
            OutputStream out = new ByteArrayOutputStream();

            new GeometryJSON().writeMultiPolygon(multiPolygon, out);

            return new JsonParser()
                    .parse(out.toString())
                    .getAsJsonObject();

        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }
}
