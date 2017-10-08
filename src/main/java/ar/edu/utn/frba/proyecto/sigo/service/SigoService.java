package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import ar.edu.utn.frba.proyecto.sigo.exception.ResourceNotFoundException;
import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class SigoService<ENTITY extends SigoDomain, PARENT_ENTITY extends SigoDomain> {

    protected Class<ENTITY> clazz;
    protected SessionFactory sessionFactory;

    public SigoService(Class<ENTITY> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    public ENTITY get(Long id){

        ENTITY result = currentSession().get(clazz, id);

        return Optional
                .ofNullable(result)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("%s with id %d%n", clazz.getSimpleName(), id)));
    }

    public ENTITY update(ENTITY newInstance) {

        ENTITY oldInstance = currentSession().get(clazz,newInstance.getId());

        preUpdateActions(newInstance, oldInstance);

        return (ENTITY) currentSession().merge(newInstance);

    }

    protected void preUpdateActions(ENTITY newInstance, ENTITY oldInstance){

    }

    public ENTITY create(ENTITY object) {

        validateCreation(object);

        preCreateActions(object);

        currentSession().save(object);

        postCreateActions(object);

        return object;
    }

    public ENTITY create(ENTITY object, PARENT_ENTITY parent) {

        validateCreation(object, parent);

        preCreateActions(object, parent);

        currentSession().save(object);

        postCreateActions(object, parent);

        return object;
    }

    protected void preCreateActions(ENTITY object, PARENT_ENTITY parent) {

    }

    protected void postCreateActions(ENTITY object, PARENT_ENTITY parent) {

    }

    protected void postCreateActions(ENTITY object) {

    }

    protected void validateCreation(ENTITY object, PARENT_ENTITY parent) {

    }

    protected void validateCreation(ENTITY airport) {
        //TODO validate unique (or catch unique constrain exception)
    }

    protected void preCreateActions(ENTITY airport) {
        //TODO
    }

    public void delete (Long id){

        ENTITY object = Optional
                .ofNullable(currentSession().get(clazz, id))
                .orElseThrow(()-> new ResourceNotFoundException(String.format("%s with id %d%n", clazz.getSimpleName(), id)));

        currentSession().delete(object);
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected void validateDeletion() {
        //TODO
    }

    public void defineGeometry(Geometry geom, Spatial domain) {

        domain.setGeom(geom);

        currentSession().update(domain);
    }

    public List<ENTITY> findAll(){
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<ENTITY> criteria = builder.createQuery(clazz);
        Root<ENTITY> root = criteria.from(clazz);
        criteria.select(root);

        return currentSession().createQuery(criteria).getResultList();
    }
}
