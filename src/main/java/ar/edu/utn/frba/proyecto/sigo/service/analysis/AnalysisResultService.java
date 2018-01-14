package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisResult;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisResultService extends SigoService<AnalysisResult, AnalysisObject>{

    @Inject
    public AnalysisResultService(HibernateUtil util) {
        super(AnalysisResult.class, util.getSessionFactory());
    }
}
