package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayTakeoffSection;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.hibernate.SessionFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Inject;
import javax.inject.Singleton;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper.*;

@Singleton
public class RunwayTakeoffSectionService extends SigoService<RunwayTakeoffSection, RunwayDirection> {

    @Inject
    public RunwayTakeoffSectionService(SessionFactory sessionFactory) {
        super(RunwayTakeoffSection.class, sessionFactory);
    }

    public SimpleFeature getStopwayFeature(RunwayDirection runwayDirection) {

        return SimpleFeatureBuilder.build(
                getStopwayFeatureSchema(),
                new Object[]{
                        calculateStopwayGeometry(runwayDirection),
                        "Stopway",
                        runwayDirection.getTakeoffSection().getStopwayLength()
                },
                runwayDirection.getTakeoffSection().getId().toString()
        );
    }

    private Polygon calculateStopwayGeometry(RunwayDirection runwayDirection) {

        Coordinate[] extremes = runwayDirection.getRunway().getGeom().norm().getCoordinates();

        double azimuth;
        Coordinate extreme1;
        Coordinate extreme4;
        Coordinate extreme2;
        Coordinate extreme3;
        Coordinate oppositeThreshold;

        azimuth = realAzimuth(runwayDirection);

        oppositeThreshold = oppositeThreshold(runwayDirection);

        double halfRunwayWidth = runwayDirection.getRunway().getWidth() / 2;

        extreme1 = move(oppositeThreshold, azimuth+90, halfRunwayWidth);
        extreme4 = move(oppositeThreshold, azimuth-90, halfRunwayWidth);;
        extreme2 = move(extreme1, azimuth, runwayDirection.getTakeoffSection().getStopwayLength());
        extreme3 = move(extreme4, azimuth, runwayDirection.getTakeoffSection().getStopwayLength());

        return new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
    }

    private SimpleFeatureType getStopwayFeatureSchema() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Stopway");
        tb.add("geom", java.awt.Polygon.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("length", Double.class);

        return tb.buildFeatureType();
    }

    public SimpleFeature getClearwayFeature(RunwayDirection runwayDirection) {

        return SimpleFeatureBuilder.build(
                getClearwayFeatureSchema(),
                new Object[]{
                        calculateClearwayGeometry(runwayDirection),
                        "Clearway",
                        runwayDirection.getTakeoffSection().getClearwayWidth(),
                        runwayDirection.getTakeoffSection().getClearwayLength(),
                },
                runwayDirection.getTakeoffSection().getId().toString()
        );
    }

    private Polygon calculateClearwayGeometry(RunwayDirection runwayDirection) {

        double azimuth;
        Coordinate extreme1;
        Coordinate extreme4;
        Coordinate extreme2;
        Coordinate extreme3;
        Coordinate oppositeThreshold;

        azimuth = realAzimuth(runwayDirection);

        oppositeThreshold = oppositeThreshold(runwayDirection);

        extreme1= move(oppositeThreshold, azimuth+90, runwayDirection.getTakeoffSection().getClearwayWidth());
        extreme4= move(oppositeThreshold, azimuth-90, runwayDirection.getTakeoffSection().getClearwayWidth());
        extreme2= move(extreme1, azimuth, runwayDirection.getTakeoffSection().getClearwayLength());
        extreme3= move(extreme4, azimuth, runwayDirection.getTakeoffSection().getClearwayLength());

        return new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
    }

    private SimpleFeatureType getClearwayFeatureSchema() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Clearway");
        tb.add("geom", java.awt.Polygon.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("width", Double.class);
        tb.add("length", Double.class);

        return tb.buildFeatureType();
    }
}
