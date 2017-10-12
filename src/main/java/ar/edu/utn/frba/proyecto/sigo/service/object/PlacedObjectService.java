package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.location.geographic.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlacedObjectService extends SigoService<PlacedObject, Region>{

    @Inject
    public PlacedObjectService(HibernateUtil hibernateUtil) {
        super(PlacedObject.class, hibernateUtil.getSessionFactory());
    }
}
