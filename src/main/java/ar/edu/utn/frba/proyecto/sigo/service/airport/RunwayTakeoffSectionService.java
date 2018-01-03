package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayTakeoffSection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.*;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import sun.misc.FloatingDecimal;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.getAzimuth;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.move;
import static com.vividsolutions.jts.geom.PrecisionModel.FLOATING;

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

    private Geometry calculateStopwayGeometry(RunwayDirection runwayDirection) {

        double azimuth = getAzimuth(runwayDirection.getStartPoint().getCoordinate(), runwayDirection.getEndPoint().getCoordinate());

        Coordinate extreme1 = move(runwayDirection.getEndPoint().getCoordinate(),azimuth + 90, runwayDirection.getRunway().getWidth()/2);
        Coordinate extreme2 = move(runwayDirection.getEndPoint().getCoordinate(),azimuth - 90, runwayDirection.getRunway().getWidth()/2);
        Coordinate extreme3 = move(extreme2,azimuth, runwayDirection.getTakeoffSection().getStopwayLength());
        Coordinate extreme4 = move(extreme1,azimuth, runwayDirection.getTakeoffSection().getStopwayLength());


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

    private Geometry calculateClearwayGeometry(RunwayDirection runwayDirection) {

        double azimuth = getAzimuth(runwayDirection.getStartPoint().getCoordinate(), runwayDirection.getEndPoint().getCoordinate());

        Coordinate extreme1 = move(runwayDirection.getEndPoint().getCoordinate(),azimuth + 90, runwayDirection.getTakeoffSection().getClearwayWidth()/2);
        Coordinate extreme2 = move(runwayDirection.getEndPoint().getCoordinate(),azimuth - 90, runwayDirection.getTakeoffSection().getClearwayWidth()/2);
        Coordinate extreme3 = move(extreme2,azimuth, runwayDirection.getTakeoffSection().getClearwayLength());
        Coordinate extreme4 = move(extreme1,azimuth, runwayDirection.getTakeoffSection().getClearwayLength());


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
