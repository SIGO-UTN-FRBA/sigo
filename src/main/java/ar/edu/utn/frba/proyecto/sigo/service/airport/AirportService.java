package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport_;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.google.common.collect.Lists;

import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import spark.QueryParamsMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Singleton
public class AirportService extends SigoService<Airport, Airport> {

    @Inject
    public AirportService(HibernateUtil hibernateUtil) {
        super(Airport.class, hibernateUtil.getSessionFactory());
    }

    public List<Runway> getRunways(Airport airport){
        return airport.getRunways();
    };

    protected void preUpdateActions(Airport newInstance, Airport oldInstance){
        newInstance.setGeom(oldInstance.getGeom());
    }

    public List<Airport> find(QueryParamsMap parameters){

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();

        CriteriaQuery<Airport> criteria = builder.createQuery(Airport.class);

        Root<Airport> airport = criteria.from(Airport.class);

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

    public SimpleFeature getFeature(Airport airport){

        SimpleFeatureType schema = getAirportFeatureSchema();

        return SimpleFeatureBuilder.build(
                schema,
                new Object[]{
                        airport.getGeom(),
                        airport.getNameFIR(),
                        airport.getCodeFIR(),
                        airport.getCodeIATA()
                },
                airport.getId().toString()
        );
    }

    private SimpleFeatureType getAirportFeatureSchema() {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Airport");
        tb.add("geom", Point.class, DefaultGeographicCRS.WGS84);
        tb.add("name", String.class);
        tb.add("codeFIR", String.class);
        tb.add("codeIATA", String.class);

        return tb.buildFeatureType();
    }
}
