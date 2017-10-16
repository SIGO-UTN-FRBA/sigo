package ar.edu.utn.frba.proyecto.sigo.service.location;

import ar.edu.utn.frba.proyecto.sigo.domain.location.political.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;

public class PoliticalLocationService extends SigoService<PoliticalLocation, PoliticalLocation> {

    @Inject
    public PoliticalLocationService(HibernateUtil hibernateUtil) {
        super(PoliticalLocation.class,hibernateUtil.getSessionFactory());
    }
}
