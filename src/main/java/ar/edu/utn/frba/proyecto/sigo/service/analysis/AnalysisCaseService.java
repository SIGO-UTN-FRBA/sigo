package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectBuilding;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividual;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectOverheadWire;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.query.Query;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

public class AnalysisCaseService extends SigoService <AnalysisCase, Analysis> {

    private AnalysisExceptionClone clone;

    @Inject
    public AnalysisCaseService(
            HibernateUtil hibernateUtil,
            AnalysisExceptionClone clone
    ) {
        super(AnalysisCase.class, hibernateUtil.getSessionFactory());
        this.clone = clone;
    }

    @Override
    protected void preCreateActions(AnalysisCase object, Analysis parent) {
        super.preCreateActions(object, parent);

        object.setExceptions(Sets.newHashSet());
        object.setObjects(Lists.newArrayList());
    }

    @Override
    protected void postCreateActions(AnalysisCase analysisCase, Analysis parent) {

        super.postCreateActions(analysisCase, parent);

        initializeObjects(analysisCase);

        initializeExceptions(analysisCase, parent);
    }

    private void initializeObjects(AnalysisCase analysisCase) {

        this.collectPlacedObject(analysisCase)
                .stream()
                .map(o -> AnalysisObject.builder()
                        .analysisCase(analysisCase)
                        .placedObject(o)
                        .included(analysisCase.getAnalysis().getParent().isObjectAnalyzed(o))
                        .build()
                )
                .forEach(o -> {
                    currentSession().save(o);
                });
    }

    private void discardObjects(AnalysisCase analysisCase) {
        analysisCase.getObjects().forEach( o -> currentSession().delete(o));
        analysisCase.getObjects().clear();
    }

    public void updateObjects(AnalysisCase analysisCase, Double radius){

        analysisCase.setSearchRadius(radius);

        discardObjects(analysisCase);

        initializeObjects(analysisCase);
    }

    private void initializeExceptions(AnalysisCase analysisCase, Analysis parent) {

        parent.getParent().getAnalysisCase().getExceptions()
                .stream()
                .map(e -> e.accept(clone))
                .forEach(e -> {

                    e.setAnalysisCase(analysisCase);

                    currentSession().save(e);
                });

    }

    private List<AnalysisException> inheritExceptions(AnalysisCase analysisCase) {
        return null;
    }

    private List<PlacedObject> collectPlacedObject(AnalysisCase analysisCase) {

        List<PlacedObject> placedObjects = Lists.newArrayList();

        Geometry buffer = analysisCase.getAerodrome().getGeom().buffer(analysisCase.getSearchRadius());

        buffer.setSRID(4326);


        placedObjects.addAll(collectPlacedObjectsOnArea(PlacedObjectIndividual.class, buffer));

        placedObjects.addAll(collectPlacedObjectsOnArea(PlacedObjectBuilding.class, buffer));

        placedObjects.addAll(collectPlacedObjectsOnArea(PlacedObjectOverheadWire.class, buffer));


        return placedObjects;
    }

    private <T extends PlacedObject> List<T> collectPlacedObjectsOnArea(Class<T> clazz, Geometry buffer) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(clazz);

        Root<T> placedObject = criteria.from(clazz);


        ParameterExpression bufferParam = builder.parameter(Geometry.class);


        Expression<Boolean> st_coveredby = builder.function("st_coveredby", Boolean.class, placedObject.get("geom"), bufferParam);

        criteria.where(builder.isTrue(st_coveredby));

        Query<T> query = currentSession().createQuery(criteria);
        query.setParameter(bufferParam, buffer);

        return query.getResultList();
    }

}
