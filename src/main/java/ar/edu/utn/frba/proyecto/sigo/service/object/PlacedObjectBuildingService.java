package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectBuilding;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectBuilding_;
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
public class PlacedObjectBuildingService extends SigoService<PlacedObjectBuilding, Region> {

    @Inject
    public PlacedObjectBuildingService(SessionFactory sessionFactory) {
        super(PlacedObjectBuilding.class, sessionFactory);
    }

    @Override
    protected void preUpdateActions(PlacedObjectBuilding newInstance, PlacedObjectBuilding oldInstance) {
        super.preUpdateActions(newInstance, oldInstance);

        newInstance.setGeom(oldInstance.getGeom());
    }

    public Stream<PlacedObjectBuilding> find(QueryParamsMap parameters){

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<PlacedObjectBuilding> criteria = builder.createQuery(PlacedObjectBuilding.class);

        Root<PlacedObjectBuilding> placedObject = criteria.from(PlacedObjectBuilding.class);

        Optional<Predicate> predicateName = Optional
                .ofNullable(parameters.get(PlacedObjectBuilding_.name.getName()).value())
                .map(v -> builder.like(placedObject.get(PlacedObjectBuilding_.name),String.format("%%%s%%",v)));

        List<Predicate> collect = Lists.newArrayList(predicateName)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultStream();
    }
}
