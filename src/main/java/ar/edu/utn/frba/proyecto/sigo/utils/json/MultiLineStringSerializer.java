package ar.edu.utn.frba.proyecto.sigo.utils.json;

import com.google.gson.*;
import com.vividsolutions.jts.geom.MultiLineString;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

public class MultiLineStringSerializer implements JsonSerializer<MultiLineString>{
    @Override
    public JsonElement serialize(MultiLineString multiLineString, Type type, JsonSerializationContext jsonSerializationContext) {
        try {
            OutputStream out = new ByteArrayOutputStream();

            new GeometryJSON().writeMultiLine(multiLineString, out);

            return new JsonParser()
                    .parse(out.toString())
                    .getAsJsonObject();

        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }
}
