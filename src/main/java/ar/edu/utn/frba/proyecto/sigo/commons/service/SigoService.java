package ar.edu.utn.frba.proyecto.sigo.commons.service;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import ar.edu.utn.frba.proyecto.sigo.exception.ResourceNotFoundException;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SigoService<ENTITY extends SigoDomain, PARENT_ENTITY extends SigoDomain> {

    protected Class<ENTITY> clazz;
    protected HibernateUtil hibernateUtil;

    public ENTITY get(Long id){

        ENTITY result = hibernateUtil.doInTransaction((Function<Session, ENTITY>) (session) -> session.get(clazz, id));

        return Optional
                .ofNullable(result)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("airport_id == %d%n",id)));
    }

    public ENTITY update(ENTITY newInstance) {

        return hibernateUtil.doInTransaction(session -> {

            ENTITY oldInstance = session.get(clazz,newInstance.getId());

            preUpdateActions(newInstance, oldInstance);

            return (ENTITY) session.merge(newInstance);
        });
    }

    protected void preUpdateActions(ENTITY newInstance, ENTITY oldInstance){

    }

    public ENTITY create(ENTITY object) {

        validateCreation(object);

        preCreateActions(object);

        hibernateUtil.doInTransaction((Consumer<Session>) session -> session.save(object));

        return object;
    }

    public ENTITY create(ENTITY object, PARENT_ENTITY parent) {

        validateCreation(object, parent);

        preCreateActions(object, parent);

        hibernateUtil.doInTransaction((Consumer<Session>) session -> session.save(object));

        return object;
    }

    protected void preCreateActions(ENTITY object, PARENT_ENTITY parent) {

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

        hibernateUtil.doInTransaction(session -> {
            ENTITY object = Optional
                    .ofNullable(session.get(clazz, id))
                    .orElseThrow(()-> new ResourceNotFoundException(String.format("airport_id == %d%n",id)));

            session.delete(object);
        });
    }

    protected void validateDeletion() {
        //TODO
    }

    public void defineGeometry(Geometry geom, Spatial domain) {
        hibernateUtil.doInTransaction(session -> {
            domain.setGeom(geom);
            session.update(domain);
        });
    }
}
