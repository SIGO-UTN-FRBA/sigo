package ar.edu.utn.frba.proyecto.sigo.main;

import ar.edu.utn.frba.proyecto.sigo.utils.json.ObjectMapperProvider;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Gson.class).toProvider(ObjectMapperProvider.class);
    }
}
