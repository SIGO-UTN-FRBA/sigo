package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle_;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

public class AnalysisObstacleService extends SigoService<AnalysisObstacle, AnalysisCase>{

    @Inject
    public AnalysisObstacleService(HibernateUtil util) {
        super(AnalysisObstacle.class, util.getSessionFactory());
    }

    public Stream<AnalysisObstacle> find(Long analysisCaseId){

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<AnalysisObstacle> criteria = builder.createQuery(AnalysisObstacle.class);

        Root<AnalysisObstacle> obstacle = criteria.from(AnalysisObstacle.class);

        criteria.where(builder.equal(obstacle.get(AnalysisObstacle_.analysisCase.getName()),analysisCaseId));

        return currentSession().createQuery(criteria).getResultStream();
    }
}
