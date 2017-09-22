package ar.edu.utn.frba.proyecto.sigo.airport;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import org.hibernate.Session;

import org.hibernate.query.criteria.internal.expression.ParameterExpressionImpl;
import org.hibernate.query.criteria.internal.predicate.PredicateImplementor;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.*;
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

}
