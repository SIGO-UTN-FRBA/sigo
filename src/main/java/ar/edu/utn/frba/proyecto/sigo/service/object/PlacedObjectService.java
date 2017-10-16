package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.location.geographic.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectBuildingSpec;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividualSpec;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectOverheadWireSpec;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectSpec;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject_;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Singleton
public class PlacedObjectService extends SigoService<PlacedObject, Region>{

    @Inject
    public PlacedObjectService(HibernateUtil hibernateUtil) {
        super(PlacedObject.class, hibernateUtil.getSessionFactory());
    }

    public List<PlacedObject> find(QueryParamsMap parameters){

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<PlacedObject> criteria = builder.createQuery(PlacedObject.class);

        Root<PlacedObject> placedObject = criteria.from(PlacedObject.class);

        Optional<Predicate> predicate1 = Optional
                .ofNullable(parameters.get(PlacedObject_.type.getName()).value())
                .map(v -> builder.equal(placedObject.get(PlacedObject_.type), Integer.valueOf(v)));

        Optional<Predicate> predicate2 = Optional
                .ofNullable(parameters.get(PlacedObject_.name.getName()).value())
                .map(v -> builder.like(placedObject.get(PlacedObject_.name),String.format("%%%s%%",v)));

        List<Predicate> collect = Lists.newArrayList(predicate1, predicate2)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultList();
    }

    @Override
    protected void preUpdateActions(PlacedObject newInstance, PlacedObject oldInstance) {
        newInstance.setSpecification(oldInstance.getSpecification());
    }

    @Override
    protected void postCreateActions(PlacedObject object) {
        createSpecification(object);
    }

    private void createSpecification(PlacedObject object) {

        PlacedObjectSpec spec;

        switch (object.getType()) {
            case BUILDING:
                spec = createBuildingSpec(object);
                break;
            case INDIVIDUAL:
                spec = createIndividualSpec(object);
                break;
            case OVERHEAD_WIRED:
                spec = createWiredSpect(object);
                break;
            default:
                spec = null;
        }

        object.setSpecification(spec);
    }

    private PlacedObjectOverheadWireSpec createWiredSpect(PlacedObject object) {

        PlacedObjectOverheadWireSpec spec = PlacedObjectOverheadWireSpec.builder()
                .placedObject(object)
                .build();

        currentSession().save(spec);

        return spec;
    }

    private PlacedObjectIndividualSpec createIndividualSpec(PlacedObject object) {

        PlacedObjectIndividualSpec spec = PlacedObjectIndividualSpec.builder()
                .placedObject(object)
                .build();

        currentSession().save(spec);

        return spec;
    }

    private PlacedObjectBuildingSpec createBuildingSpec(PlacedObject object) {

        PlacedObjectBuildingSpec spec = PlacedObjectBuildingSpec.builder()
                .placedObject(object)
                .build();

        currentSession().save(spec);

        return spec;
    }
}
