package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectOwner;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;

public class ObjectOwnerService extends SigoService<PlacedObjectOwner,PlacedObjectOwner> {

    @Inject
    public ObjectOwnerService(HibernateUtil hibernateUtil) {
        super(PlacedObjectOwner.class, hibernateUtil.getSessionFactory());
    }
}
