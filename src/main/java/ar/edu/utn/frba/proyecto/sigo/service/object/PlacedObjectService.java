package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlacedObjectService extends SigoService<PlacedObject, Region> {

    @Inject
    public PlacedObjectService(SessionFactory sessionFactory) {
        super(PlacedObject.class, sessionFactory);
    }

}
