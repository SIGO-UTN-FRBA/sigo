package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport_;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;

import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Singleton
public class AirportService extends SigoService<Airport, Airport> {

    @Inject
    public AirportService(HibernateUtil hibernateUtil) {
        super(Airport.class, hibernateUtil.getSessionFactory());
    }

    public List<Runway> getRunways(Airport airport){
        return airport.getRunways();
    };

    protected void preUpdateActions(Airport newInstance, Airport oldInstance){
        newInstance.setGeom(oldInstance.getGeom());
    }

    public List<Airport> find(QueryParamsMap parameters){

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

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
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultList();
    }
}
