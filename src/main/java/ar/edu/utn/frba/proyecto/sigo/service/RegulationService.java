package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.domain.Regulation;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;

import javax.inject.Inject;

public class RegulationService extends SigoService <Regulation, Regulation>{

    @Inject
    public RegulationService(HibernateUtil hibernateUtil) {
        super(Regulation.class, hibernateUtil.getSessionFactory());
    }
}
