package ar.edu.utn.frba.proyecto.sigo.spark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    public static final Gson GSON = new GsonBuilder().create();

    @Override
    public String render(Object model) {
        return GSON.toJson(model);
    }


}
