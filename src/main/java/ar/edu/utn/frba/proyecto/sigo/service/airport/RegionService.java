package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.location.geographic.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.location.geographic.State;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RegionService extends SigoService<Region, State> {

    @Inject
    public RegionService(HibernateUtil hibernateUtil) {
        super(Region.class, hibernateUtil.getSessionFactory());
    }
}
