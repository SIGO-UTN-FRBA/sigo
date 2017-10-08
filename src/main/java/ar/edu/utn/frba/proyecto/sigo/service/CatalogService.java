package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.domain.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.RunwaySurface;
import ar.edu.utn.frba.proyecto.sigo.domain.RunwayTypeLetterICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.RunwayTypeNumberICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Singleton
public class CatalogService {

    private SessionFactory sessionFactory;

    @Inject
    public CatalogService(
        HibernateUtil hibernateUtil
    ){
        this.sessionFactory = hibernateUtil.getSessionFactory();
    }

    public List<RunwaySurface> findAllRunwaySurfaces() {
        return this.findAll(RunwaySurface.class);
    }

    public List<Regulation> findAllAirportRegulations() {
        return this.findAll(Regulation.class);
    }

    public List<RunwayTypeLetterICAOAnnex14> findAllRunwayTypeLetterICAO(){
        return this.findAll(RunwayTypeLetterICAOAnnex14.class);
    }

    public List<RunwayTypeNumberICAOAnnex14> findAllRunwayTypeNumberICAO(){
        return this.findAll(RunwayTypeNumberICAOAnnex14.class);
    }

    private <T> List<T> findAll(Class<T> clazz){

        Session currentSession = this.sessionFactory.getCurrentSession();

        CriteriaBuilder builder = currentSession.getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        Root<T> root = criteria.from(clazz);
        criteria.select(root);

        return currentSession.createQuery(criteria).getResultList();
    }
}
