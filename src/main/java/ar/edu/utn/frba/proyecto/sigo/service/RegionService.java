package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.domain.Region;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;

import javax.inject.Inject;

public class RegionService extends SigoService<Region, Region> {

    @Inject
    public RegionService(HibernateUtil hibernateUtil) {
        super(Region.class, hibernateUtil.getSessionFactory());
    }
}
