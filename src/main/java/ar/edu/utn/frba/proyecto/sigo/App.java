package ar.edu.utn.frba.proyecto.sigo;

import ar.edu.utn.frba.proyecto.sigo.router.airport.RouterModule;
import ar.edu.utn.frba.proyecto.sigo.persistence.PersistenceModule;
import ar.edu.utn.frba.proyecto.sigo.main.MainModule;
import ar.edu.utn.frba.proyecto.sigo.parameters.ParametersModuleProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App 
{
    public static void main( String[] args )
    {
        log.info("Initializing Modules...");

        Injector injector = Guice.createInjector(
                ParametersModuleProvider.get(),
                new MainModule(),
                new PersistenceModule(),
                new RouterModule()
        );

        ApiContext context = injector.getBinding(ApiContext.class).getProvider().get();

        log.info("Defining API context...");

        context.init();
    }
}
