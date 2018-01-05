package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayTakeoffSection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.azimuth;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.move;

@Singleton
public class RunwayTakeoffSectionService extends SigoService<RunwayTakeoffSection, RunwayDirection> {

    @Inject
    public RunwayTakeoffSectionService(HibernateUtil hibernateUtil) {
        super(RunwayTakeoffSection.class, hibernateUtil.getSessionFactory());
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

        double azimuth = azimuth(extremes[0],extremes[3]);

        Coordinate extreme1;
        Coordinate extreme4;
        Coordinate extreme2;
        Coordinate extreme3;

        if(runwayDirection.getNumber()<18){
            extreme1 = extremes[0];
            extreme4 = extremes[1];
            extreme2 = move(extreme1, azimuth, -1 *runwayDirection.getTakeoffSection().getStopwayLength());
            extreme3 = move(extreme4, azimuth, -1 *runwayDirection.getTakeoffSection().getStopwayLength());
        } else {
            extreme1 = extremes[2];
            extreme4 = extremes[3];
            extreme2 = move(extreme1, azimuth, runwayDirection.getTakeoffSection().getStopwayLength());
            extreme3 = move(extreme4, azimuth, runwayDirection.getTakeoffSection().getStopwayLength());
        }

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

        Coordinate[] extremes = runwayDirection.getRunway().getGeom().norm().getCoordinates();

        double azimuth = azimuth(extremes[0],extremes[3]);

        Coordinate extreme1;
        Coordinate extreme4;
        Coordinate extreme2;
        Coordinate extreme3;

        if(runwayDirection.getNumber()<18){
            extreme1= move(extremes[0], azimuth+90, runwayDirection.getTakeoffSection().getClearwayWidth());
            extreme4= move(extremes[1], azimuth-90, runwayDirection.getTakeoffSection().getClearwayWidth());
            extreme2= move(extreme1, azimuth, -1*runwayDirection.getTakeoffSection().getClearwayLength());
            extreme3= move(extreme4, azimuth, -1*runwayDirection.getTakeoffSection().getClearwayLength());
        } else {
            extreme1= move(extremes[2], azimuth-90, runwayDirection.getTakeoffSection().getClearwayWidth());
            extreme4= move(extremes[3], azimuth+90, runwayDirection.getTakeoffSection().getClearwayWidth());
            extreme2= move(extreme1, azimuth, 1*runwayDirection.getTakeoffSection().getClearwayLength());
            extreme3= move(extreme4, azimuth, 1*runwayDirection.getTakeoffSection().getClearwayLength());
        }

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
