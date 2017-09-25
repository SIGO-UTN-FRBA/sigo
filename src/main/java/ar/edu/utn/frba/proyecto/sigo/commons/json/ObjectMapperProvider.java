package ar.edu.utn.frba.proyecto.sigo.commons.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;

import javax.inject.Provider;
import javax.inject.Singleton;


@Singleton
public class ObjectMapperProvider implements Provider<Gson>{
    @Override
    public Gson get() {
        return new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .serializeNulls()
                .registerTypeAdapter(Point.class, new PointSerializer())
                .registerTypeAdapter(Point.class, new PointDeserializer())
                .registerTypeAdapter(MultiLineString.class, new MultiLineStringDeserializer())
                .registerTypeAdapter(MultiLineString.class, new MultiLineStringSerializer())
                .create();
    }
}
