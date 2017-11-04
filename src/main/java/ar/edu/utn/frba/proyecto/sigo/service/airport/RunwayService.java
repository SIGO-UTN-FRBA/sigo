package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.List;


@Singleton
public class RunwayService extends SigoService<Runway, Airport> {

    @Override
    protected void preCreateActions(Runway instance) {
        super.preCreateActions(instance);
    }

    @Inject
    public RunwayService(HibernateUtil hibernateUtil){
        super(Runway.class, hibernateUtil.getSessionFactory());
    }

    protected void preUpdateActions(Runway newInstance, Runway oldInstance){
        newInstance.setGeom(oldInstance.getGeom());
    }

    @Override
    protected void preCreateActions(Runway runway, Airport airport) {
        airport.addRunway(runway);
    }

    public List<RunwayDirection> getDirections(Runway runway) {
        return runway.getDirections();
    }

    public SimpleFeature getFeature(Runway runway) {

        return SimpleFeatureBuilder.build(
                getFeatureSchema(),
                new Object[]{
                        runway.getGeom(),
                        "Runway",
                        runway.getName(),
                        runway.getSurface().description(),
                        runway.getWidth(),
                        runway.getLength()
                },
                runway.getId().toString()
        );
    }

    private SimpleFeatureType getFeatureSchema() {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Runway");
        tb.add("geom", Polygon.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("name", String.class);
        tb.add("surface", String.class);
        tb.add("width", Double.class);
        tb.add("length", Double.class);

        return tb.buildFeatureType();
    }
}
