package ar.edu.utn.frba.proyecto.sigo.airport;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;

import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.Airport_;
import ar.edu.utn.frba.proyecto.sigo.exceptions.ResourceNotFoundException;
import ar.edu.utn.frba.proyecto.sigo.exceptions.SigoException;
import com.google.common.collect.Lists;
import org.hibernate.Session;

import org.hibernate.resource.transaction.spi.TransactionStatus;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.*;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Singleton
public class AirportService {

    private final HibernateUtil hibernateUtil;

    @Inject
    public AirportService(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    public List<Airport> find(QueryParamsMap parameters){

        List<Airport> results;

        try(Session session = this.hibernateUtil.getSessionFactory().openSession()){

            session.getTransaction().begin();

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Airport> criteria = builder.createQuery(Airport.class);

            Root<Airport> airport = criteria.from(Airport.class);

            Optional<Predicate> predicate1 = Optional
                    .ofNullable(parameters.get(Airport_.nameFIR.getName()).value())
                    .map(v -> builder.equal(airport.get(Airport_.nameFIR), v));

            Optional<Predicate> predicate2 = Optional
                    .ofNullable(parameters.get(Airport_.codeFIR.getName()).value())
                    .map(v -> builder.equal(airport.get(Airport_.codeFIR), v));

            List<Predicate> collect = Lists.newArrayList(predicate1, predicate2)
                    .stream()
                    .filter(p -> p.isPresent())
                    .map( p -> p.get())
                    .collect(toList());

            criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

            results = session.createQuery(criteria).getResultList();

            session.getTransaction().commit();

        }

        return results;
    }

    public Airport get(Long id){

        try(Session session = this.hibernateUtil.getSessionFactory().openSession()){
            return Optional
                    .ofNullable(session.get(Airport.class, id))
                    .orElseThrow(()-> new ResourceNotFoundException(String.format("airport_id == %d%n",id)));
        }
    }

    public Airport update(Airport instance){

        Airport merged;

        try(Session session = this.hibernateUtil.getSessionFactory().openSession()){

            try{
                session.getTransaction().begin();

                merged = (Airport) session.merge(instance);

                session.getTransaction().commit();

            } catch (Exception e){
                if ( session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK ) {
                    session.getTransaction().rollback();
                }

                throw new SigoException(e);
            }
        }

        return merged;
    }

    public void delete (Long id){

        try(Session session = this.hibernateUtil.getSessionFactory().openSession()){

            try{
                session.getTransaction().begin();

                Airport airport = Optional
                        .ofNullable(session.get(Airport.class, id))
                        .orElseThrow(()-> new ResourceNotFoundException(String.format("airport_id == %d%n",id)));

                session.delete(airport);

                session.getTransaction().commit();

            } catch (Exception e){
                if ( session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK ) {
                    session.getTransaction().rollback();
                }
                throw new SigoException(e);
            }

        }
    }

    public Airport create(Airport airport) {

        try(Session session = this.hibernateUtil.getSessionFactory().openSession()){

            try{
                session.getTransaction().begin();

                session.save(airport);

                session.getTransaction().commit();

            } catch (Exception e){
                if ( session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK ) {
                    session.getTransaction().rollback();
                }

                throw new SigoException(e);
            }

            return airport;
        }


    }
}
