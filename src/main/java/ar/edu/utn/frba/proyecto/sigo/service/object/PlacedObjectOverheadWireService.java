package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectOverheadWire;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlacedObjectOverheadWireService extends SigoService<PlacedObjectOverheadWire, Region> {

    @Inject
    public PlacedObjectOverheadWireService(SessionFactory sessionFactory) {
        super(PlacedObjectOverheadWire.class, sessionFactory);
    }
}
