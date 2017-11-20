package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisExceptionService extends SigoService<AnalysisException, AnalysisCase>{

    @Inject
    public AnalysisExceptionService(HibernateUtil hibernateUtil) {
        super(AnalysisException.class, hibernateUtil.getSessionFactory());
    }
}
