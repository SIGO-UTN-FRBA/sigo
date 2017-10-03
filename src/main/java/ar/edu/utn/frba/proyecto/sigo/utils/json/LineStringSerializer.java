package ar.edu.utn.frba.proyecto.sigo.utils.json;

import com.google.gson.*;
import com.vividsolutions.jts.geom.LineString;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

public class LineStringSerializer implements JsonSerializer<LineString>{
    @Override
    public JsonElement serialize(LineString lineString, Type type, JsonSerializationContext jsonSerializationContext) {
        try {
            OutputStream out = new ByteArrayOutputStream();

            new GeometryJSON().writeLine(lineString, out);

            return new JsonParser()
                    .parse(out.toString())
                    .getAsJsonObject();

        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }
}
