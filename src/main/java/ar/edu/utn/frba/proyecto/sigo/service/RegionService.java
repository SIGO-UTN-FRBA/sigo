package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.domain.location.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.location.State;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RegionService extends SigoService<Region, State> {

    @Inject
    public RegionService(HibernateUtil hibernateUtil) {
        super(Region.class, hibernateUtil.getSessionFactory());
    }
}
