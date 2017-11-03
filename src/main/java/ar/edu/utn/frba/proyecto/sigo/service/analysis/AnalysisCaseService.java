package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport_;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCaseStatuses;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase_;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectBuilding;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividual;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividual_;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectOverheadWire;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.query.Query;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class AnalysisCaseService extends SigoService <AnalysisCase, AnalysisCase> {

    @Inject
    public AnalysisCaseService(
            HibernateUtil hibernateUtil
    ) {
        super(AnalysisCase.class, hibernateUtil.getSessionFactory());
    }

    public List<AnalysisCase> find(QueryParamsMap parameters) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<AnalysisCase> criteria = builder.createQuery(AnalysisCase.class);

        Root<AnalysisCase> analysisCase = criteria.from(AnalysisCase.class);

        Join<AnalysisCase, Airport> airport = analysisCase.join(AnalysisCase_.aerodrome);

        Optional<Predicate> predicate1 = Optional
                .ofNullable(parameters.get(Airport_.nameFIR.getName()).value())
                .map(v -> builder.like(airport.get(Airport_.nameFIR), String.format("%%%s%%",v)));

        Optional<Predicate> predicate2 = Optional
                .ofNullable(parameters.get(Airport_.codeFIR.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeFIR), v));

        Optional<Predicate> predicate3 = Optional
                .ofNullable(parameters.get(Airport_.codeIATA.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeIATA),v));

        List<Predicate> collect = Lists.newArrayList(predicate1, predicate2, predicate3)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultList();
    }

    @Override
    protected void preCreateActions(AnalysisCase object, AnalysisCase parent) {
        super.preCreateActions(object, parent);

        object.setBaseCase(parent);
        object.setAerodrome(parent.getAerodrome());
        object.setStatus(AnalysisCaseStatuses.PENDING);
        object.setExceptions(Sets.newHashSet());
        object.setObjects(Lists.newArrayList());
    }

    @Override
    protected void postCreateActions(AnalysisCase object, AnalysisCase parent) {
        super.postCreateActions(object, parent);

        initializeObjects(object);
    }

    private void initializeObjects(AnalysisCase analysisCase) {

        this.collectPlacedObject(analysisCase)
                .stream()
                .map(o -> AnalysisObject.builder()
                        .analysisCase(analysisCase)
                        .placedObject(o)
                        .build()
                )
                .forEach(o -> {
                    currentSession().save(o);
                    analysisCase.getObjects().add(o);
                });
    }

    private void discardObjects(AnalysisCase analysisCase) {
        analysisCase.getObjects().forEach( o -> currentSession().delete(o));
        analysisCase.getObjects().clear();
    }

    @Override
    protected void validateCreation(AnalysisCase object, AnalysisCase parent) {
        super.validateCreation(object, parent);
        //TODO validar que no existe otro caso abierto
    }

    public void updateObjects(AnalysisCase analysisCase){

        discardObjects(analysisCase);

        initializeObjects(analysisCase);
    }



    public List<PlacedObject> collectPlacedObject(AnalysisCase analysisCase) {

        List<PlacedObject> placedObjects = Lists.newArrayList();

        Double ratio = 0.1D;

        Geometry buffer = analysisCase.getAerodrome().getGeom().buffer(ratio);

        buffer.setSRID(4326);


        placedObjects.addAll(collectPlacedObjectsOnArea(PlacedObjectIndividual.class, buffer));

        placedObjects.addAll(collectPlacedObjectsOnArea(PlacedObjectBuilding.class, buffer));

        placedObjects.addAll(collectPlacedObjectsOnArea(PlacedObjectOverheadWire.class, buffer));


        return placedObjects;
    }

    private <T extends PlacedObject> List<T> collectPlacedObjectsOnArea(Class<T> clazz, Geometry buffer) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(clazz);

        Root<T> individual = criteria.from(clazz);


        ParameterExpression bufferParam = builder.parameter(Geometry.class);


        Expression<Boolean> st_coveredby = builder.function("st_coveredby", Boolean.class, individual.get(PlacedObjectIndividual_.geom.getName()), bufferParam);

        criteria.where(builder.isTrue(st_coveredby));

        Query<T> query = currentSession().createQuery(criteria);
        query.setParameter(bufferParam, buffer);

        return query.getResultList();
    }
}
