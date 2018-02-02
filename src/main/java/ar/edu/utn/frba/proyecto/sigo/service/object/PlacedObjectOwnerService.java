package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectOwner;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public class PlacedObjectOwnerService extends SigoService<PlacedObjectOwner,PlacedObjectOwner>{

    @Inject
    public PlacedObjectOwnerService(SessionFactory sessionFactory) {
        super(PlacedObjectOwner.class, sessionFactory);
    }
}
