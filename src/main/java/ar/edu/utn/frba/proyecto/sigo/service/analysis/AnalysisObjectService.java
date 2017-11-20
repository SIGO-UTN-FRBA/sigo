package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;

public class AnalysisObjectService extends SigoService<AnalysisObject, AnalysisCase>{

    @Inject
    public AnalysisObjectService(HibernateUtil hibernateUtil) {
        super(AnalysisObject.class, hibernateUtil.getSessionFactory());
    }
}
