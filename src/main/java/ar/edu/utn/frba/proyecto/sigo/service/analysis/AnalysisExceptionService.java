package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisExceptionService extends SigoService<AnalysisException, AnalysisCase>{

    @Inject
    public AnalysisExceptionService(SessionFactory sessionFactory) {
        super(AnalysisException.class, sessionFactory);
    }
}
