package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport_;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.hibernate.SessionFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Singleton
public class AirportService extends SigoService<Airport, Airport> {

    @Inject
    public AirportService(SessionFactory sessionFactory) {
        super(Airport.class, sessionFactory);
    }

    public List<Runway> getRunways(Airport airport){
        return airport.getRunways();
    };

    protected void preUpdateActions(Airport newInstance, Airport oldInstance){
        newInstance.setGeom(oldInstance.getGeom());
    }

    public Stream<Airport> find(QueryParamsMap parameters){

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<Airport> criteria = builder.createQuery(Airport.class);

        Root<Airport> airport = criteria.from(Airport.class);

        Optional<Predicate> predicateNameFIR = Optional
                .ofNullable(parameters.get(Airport_.nameFIR.getName()).value())
                .map(v -> builder.like(airport.get(Airport_.nameFIR), String.format("%%%s%%",v)));

        Optional<Predicate> predicateCodeFIR = Optional
                .ofNullable(parameters.get(Airport_.codeFIR.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeFIR), v));

        Optional<Predicate> predicateCodeIATA = Optional
                .ofNullable(parameters.get(Airport_.codeIATA.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeIATA),v));

        Optional<Predicate> predicateCodeLocal = Optional
                .ofNullable(parameters.get(Airport_.codeLocal.getName()).value())
                .map(v -> builder.equal(airport.get(Airport_.codeLocal),v));

        List<Predicate> collect = Lists.newArrayList(predicateNameFIR, predicateCodeFIR, predicateCodeIATA, predicateCodeLocal)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        criteria.where(builder.and(collect.toArray(new Predicate[collect.size()])));

        return currentSession().createQuery(criteria).getResultStream();
    }

    public SimpleFeature getFeature(Airport airport){

        return SimpleFeatureBuilder.build(
                getFeatureSchema(),
                new Object[]{
                        airport.getGeom(),
                        "Airport",
                        airport.getNameFIR(),
                        airport.getCodeFIR(),
                        airport.getCodeIATA(),
                        airport.getCodeLocal()
                },
                airport.getId().toString()
        );
    }

    private SimpleFeatureType getFeatureSchema() {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Airport");
        tb.add("geom", Point.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("name", String.class);
        tb.add("codeFIR", String.class);
        tb.add("codeIATA", String.class);
        tb.add("codeLocal", String.class);

        return tb.buildFeatureType();
    }
}
