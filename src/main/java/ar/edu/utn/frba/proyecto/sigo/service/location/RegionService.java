package ar.edu.utn.frba.proyecto.sigo.service.location;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.State;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;

public class RegionService extends SigoService<Region, State> {

    @Inject
    public RegionService(HibernateUtil hibernateUtil) {
        super(Region.class, hibernateUtil.getSessionFactory());
    }
}
