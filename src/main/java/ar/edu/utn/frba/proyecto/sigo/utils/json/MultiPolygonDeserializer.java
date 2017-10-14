package ar.edu.utn.frba.proyecto.sigo.utils.json;

import com.google.gson.*;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

public class MultiPolygonDeserializer implements JsonDeserializer<MultiPolygon> {

    @Override
    public MultiPolygon deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try{
            return Optional.ofNullable(new GeometryJSON().readMultiPolygon(jsonElement.toString()))
                        .orElseThrow(()-> new Exception("Invalid MultiPolygon geometry entered."));

        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
