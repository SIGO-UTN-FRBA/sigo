package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.router.airport.*;
import ar.edu.utn.frba.proyecto.sigo.router.object.CatalogObjectRouter;
import ar.edu.utn.frba.proyecto.sigo.router.object.PlacedObjectRouter;
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

        routerBinder.addBinding().to(AirportRouter.class);
        routerBinder.addBinding().to(RunwayRouter.class);
        routerBinder.addBinding().to(RunwayDirectionRouter.class);
        routerBinder.addBinding().to(CatalogAirportRouter.class);
        routerBinder.addBinding().to(RegionRouter.class);
        routerBinder.addBinding().to(CatalogObjectRouter.class);
        routerBinder.addBinding().to(PlacedObjectRouter.class);
    }
}
