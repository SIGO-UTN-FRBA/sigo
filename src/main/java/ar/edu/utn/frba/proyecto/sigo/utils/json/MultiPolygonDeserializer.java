package ar.edu.utn.frba.proyecto.sigo.utils.json;

import com.google.gson.*;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;
import java.lang.reflect.Type;

public class MultiPolygonDeserializer implements JsonDeserializer<MultiPolygon> {

    @Override
    public MultiPolygon deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try{
            return new GeometryJSON().readMultiPolygon(jsonElement.toString());

        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }
}
