package ar.edu.utn.frba.proyecto.sigo.router.airport;

import ar.edu.utn.frba.proyecto.sigo.service.airport.AirportService;
import ar.edu.utn.frba.proyecto.sigo.service.airport.AirportTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayService;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDirectionTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class RouterModule extends AbstractModule {

    @Override
    protected void configure() {

        Multibinder<Router> routerBinder = Multibinder.newSetBinder(binder(), Router.class);

        bind(AirportRouter.class);
        bind(AirportService.class);
        bind(AirportTranslator.class);
        bind(RunwayRouter.class);
        bind(RunwayService.class);
        bind(RunwayTranslator.class);
        bind(RunwayDirectionRouter.class);
        bind(RunwayDirectionService.class);
        bind(RunwayDirectionTranslator.class);
        bind(CatalogRouter.class);
        bind(RegionRouter.class);

        routerBinder.addBinding().to(AirportRouter.class);
        routerBinder.addBinding().to(RunwayRouter.class);
        routerBinder.addBinding().to(RunwayDirectionRouter.class);
        routerBinder.addBinding().to(CatalogRouter.class);
        routerBinder.addBinding().to(RegionRouter.class);
    }
}
