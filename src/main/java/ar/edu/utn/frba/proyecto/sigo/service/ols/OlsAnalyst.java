package ar.edu.utn.frba.proyecto.sigo.service.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Data
public abstract class OlsAnalyst {

    protected AnalysisCase analysisCase;
    protected SessionFactory sessionFactory;

    public OlsAnalyst(AnalysisCase analysisCase, SessionFactory sessionFactory) {
        this.analysisCase = analysisCase;
        this.sessionFactory = sessionFactory;
    }

    public void analyze(){

        initializeSurfaces();

8        defineObstacles();

        //applyExceptions();

        getCurrentSession().save(analysisCase);
    }

    protected abstract void applyExceptions();

    protected abstract void defineObstacles();

    protected abstract void initializeSurfaces();

    protected Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}
