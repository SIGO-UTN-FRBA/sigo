package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividual;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividual_;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;
import org.hibernate.SessionFactory;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Singleton
public class PlacedObjectIndividualService extends SigoService<PlacedObjectIndividual, Region> {

    @Inject
    public PlacedObjectIndividualService(SessionFactory sessionFactory) {
        super(PlacedObjectIndividual.class, sessionFactory);
    }

    @Override
    protected void preUpdateActions(PlacedObjectIndividual newInstance, PlacedObjectIndividual oldInstance) {
        super.preUpdateActions(newInstance, oldInstance);

        newInstance.setGeom(oldInstance.getGeom());
    }

    public Stream<PlacedObjectIndividual> find(QueryParamsMap parameters){

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<PlacedObjectIndividual> criteria = builder.createQuery(PlacedObjectIndividual.class);

        Root<PlacedObjectIndividual> placedObject = criteria.from(PlacedObjectIndividual.class);

        Optional<Predicate> predicateName = Optional
                .ofNullable(parameters.get(PlacedObjectIndividual_.name.getName()).value())
                .map(v -> builder.like(placedObject.get(PlacedObjectIndividual_.name),String.format("%%%s%%",v)));

        List<Predicate> collect = Lists.newArrayList(predicateName)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultStream();
    }
}
