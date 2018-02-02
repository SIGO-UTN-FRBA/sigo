package ar.edu.utn.frba.proyecto.sigo.service.location;

import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocationType;
import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocationType_;
import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation_;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class PoliticalLocationService extends SigoService<PoliticalLocation, PoliticalLocation> {

    @Inject
    public PoliticalLocationService(SessionFactory sessionFactory) {
        super(PoliticalLocation.class, sessionFactory);
    }


    public List<PoliticalLocation> find(QueryParamsMap parameters) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<PoliticalLocation> criteria = builder.createQuery(PoliticalLocation.class);

        Root<PoliticalLocation> root = criteria.from(PoliticalLocation.class);

        Join<PoliticalLocation, PoliticalLocationType> locationJoin = root.join(PoliticalLocation_.type.getName());

        criteria.where(
                builder.equal(
                    locationJoin.get(PoliticalLocationType_.name.getName()),
                    parameters.value("type")
                )
        );

        return currentSession().createQuery(criteria).getResultList();
    }
}
