package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwaySurfaces;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeLetters;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
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
public class CatalogAirportService {

    private SessionFactory sessionFactory;

    @Inject
    public CatalogAirportService(
        HibernateUtil hibernateUtil
    ){
        this.sessionFactory = hibernateUtil.getSessionFactory();
    }

    public RunwaySurfaces[] findAllRunwaySurfaces() {
        return RunwaySurfaces.values();
    }

    public List<Regulation> findAllAirportRegulations() {
        return this.findAll(Regulation.class);
    }

    public ICAOAnnex14RunwayCodeLetters[] findAllRunwayTypeLetterICAO(){
        return ICAOAnnex14RunwayCodeLetters.values();
    }

    public ICAOAnnex14RunwayCodeNumbers[] findAllRunwayTypeNumberICAO(){
        return ICAOAnnex14RunwayCodeNumbers.values();
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
