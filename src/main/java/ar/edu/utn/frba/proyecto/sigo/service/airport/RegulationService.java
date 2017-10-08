package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RegulationService extends SigoService<Regulation, Regulation> {

    @Inject
    public RegulationService(HibernateUtil hibernateUtil) {
        super(Regulation.class, hibernateUtil.getSessionFactory());
    }
}
