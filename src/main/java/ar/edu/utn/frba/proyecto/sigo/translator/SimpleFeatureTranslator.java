package ar.edu.utn.frba.proyecto.sigo.translator;

import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Singleton
public class SimpleFeatureTranslator extends SigoTranslator<SimpleFeature,JsonObject> {

    @Inject
    public SimpleFeatureTranslator(Gson gson) {
        super(gson, JsonObject.class, SimpleFeature.class);
    }

    @Override
    public JsonObject getAsDTO(SimpleFeature feature) {
        try(OutputStream outputStream = new ByteArrayOutputStream()) {
            int decimals = 14;
            GeometryJSON gjson = new GeometryJSON(decimals);

            new FeatureJSON(gjson).writeFeature(feature, outputStream);

            return objectMapper.fromJson(outputStream.toString(),JsonObject.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new SigoException(e);
        }
    }

    @Override
    public SimpleFeature getAsDomain(JsonObject geoJSON) {
        try (InputStream stream = new ByteArrayInputStream(geoJSON.toString().getBytes(StandardCharsets.UTF_8.name()))){
            int decimals = 14;
            GeometryJSON gjson = new GeometryJSON(decimals);

            return new FeatureJSON(gjson).readFeature(stream);

        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidParameterException("Malformed geometry.",e);
        }
    }
}
