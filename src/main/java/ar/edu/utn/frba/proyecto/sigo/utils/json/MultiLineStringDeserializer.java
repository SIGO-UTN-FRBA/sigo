package ar.edu.utn.frba.proyecto.sigo.utils.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.vividsolutions.jts.geom.MultiLineString;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

public class MultiLineStringDeserializer implements JsonDeserializer<MultiLineString> {
    @Override
    public MultiLineString deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        try{
            return Optional.ofNullable(new GeometryJSON().readMultiLine(jsonElement.toString()))
                        .orElseThrow(()-> new Exception("Invalid MultiLineString geometry entered."));

        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}
