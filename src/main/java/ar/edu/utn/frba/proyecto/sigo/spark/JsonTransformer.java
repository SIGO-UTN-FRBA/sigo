package ar.edu.utn.frba.proyecto.sigo.spark;

import com.google.gson.Gson;
import spark.ResponseTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JsonTransformer implements ResponseTransformer  {

    private Gson objectMapper;

    @Inject
    public JsonTransformer(Gson gson){
        this.objectMapper = gson;
    }

    @Override
    public String render(Object model) {
        return objectMapper.toJson(model);
    }

}
