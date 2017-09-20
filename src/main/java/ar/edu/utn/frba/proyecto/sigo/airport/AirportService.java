package ar.edu.utn.frba.proyecto.sigo.airport;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.hibernate.Session;


import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Inject))
public class AirportService {

    private final HibernateUtil hibernateUtil;

    public List<Airport> findAll(){

        List<Airport> results;

        try(Session session = hibernateUtil.getSessionFactory().openSession()){

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Airport> criteria = builder.createQuery(Airport.class);

            criteria.from(Airport.class);

            results = session.createQuery(criteria).getResultList();

            session.close();
        }

        return results;
    }

}
