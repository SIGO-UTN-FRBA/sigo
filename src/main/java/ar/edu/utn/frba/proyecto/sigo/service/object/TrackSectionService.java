package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.object.TrackSection;
import ar.edu.utn.frba.proyecto.sigo.domain.object.TrackSection_;
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
public class TrackSectionService extends SigoService<TrackSection, SigoDomain> {

    @Inject
    public TrackSectionService(SessionFactory sessionFactory) {
        super(TrackSection.class, sessionFactory);
    }

    @Override
    protected void preUpdateActions(TrackSection newInstance, TrackSection oldInstance) {
        super.preUpdateActions(newInstance, oldInstance);

        newInstance.setGeom(oldInstance.getGeom());
    }

    public Stream<TrackSection> find(QueryParamsMap parameters){

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<TrackSection> criteria = builder.createQuery(TrackSection.class);

        Root<TrackSection> placedObject = criteria.from(TrackSection.class);

        Optional<Predicate> predicateName = Optional
                .ofNullable(parameters.get(TrackSection_.name.getName()).value())
                .map(v -> builder.like(placedObject.get(TrackSection_.name),String.format("%%%s%%",v)));

        List<Predicate> collect = Lists.newArrayList(predicateName)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultStream();
    }
}
