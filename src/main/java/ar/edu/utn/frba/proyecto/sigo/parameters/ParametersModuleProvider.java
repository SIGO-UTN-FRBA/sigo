package ar.edu.utn.frba.proyecto.sigo.parameters;

import com.github.racc.tscg.TypesafeConfigModule;
import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParametersModuleProvider {

    public static AbstractModule get() {

        Config config = ConfigFactory.load("application.conf");

        return TypesafeConfigModule.fromConfigWithPackage(config, "ar.edu.utn.frba.proyecto.sigo");
    }
}
