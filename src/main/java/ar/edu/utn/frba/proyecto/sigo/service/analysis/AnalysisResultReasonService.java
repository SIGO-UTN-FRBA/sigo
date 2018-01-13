package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisResultReason;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;

public class AnalysisResultReasonService extends SigoService<AnalysisResultReason, AnalysisResultReason>{

    @Inject
    public AnalysisResultReasonService(HibernateUtil util) {
        super(AnalysisResultReason.class, util.getSessionFactory());
    }
}
