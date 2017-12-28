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

    private Polygon calculateStopwayGeometry(RunwayDirection runwayDirection) {

        final Coordinate[] runwayCoordinates = runwayDirection.getRunway().getGeom().getCoordinates();

        /*List<Coordinate> extremes = Arrays.stream(runwayCoordinates)
                .distinct()
                .sorted((i, j) -> {
                    if (runwayDirection.getGeom().getCoordinate().distance(i) > runwayDirection.getGeom().getCoordinate().distance(j))
                        return 1;
                    else
                        return -1;
                })
                .collect(Collectors.toList());*/

        /*  ARRAY[(st_project(trd.end_point::geography, 50, st_azimuth(trd.start_point::geography, trd.end_point::geography)+ (pi()/2)))::geometry,
  (st_project(trd.end_point::geography, 50, st_azimuth(trd.start_point::geography, trd.end_point::geography)- (pi()/2)))::geometry,
  (st_project(st_project(trd.end_point::geography, 50, st_azimuth(trd.start_point::geography, trd.end_point::geography)- (pi()/2)), 50, st_azimuth(trd.start_point::geography, trd.end_point::geography)))::geometry,
  (st_project(st_project(trd.end_point::geography, 50, st_azimuth(trd.start_point::geography, trd.end_point::geography)+ (pi()/2)), 50, st_azimuth(trd.start_point::geography, trd.end_point::geography)))::geometry,
  (st_project(trd.end_point::geography, 50, st_azimuth(trd.start_point::geography, trd.end_point::geography)+ (pi()/2)))::geometry])))
*/
        List<Coordinate> extremes;
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

        final Coordinate[] runwayCoordinates = runwayDirection.getRunway().getGeom().getCoordinates();

        List<Coordinate> extremes = Arrays.stream(runwayCoordinates)
                .distinct()
                .sorted((i, j) -> {
                    if (runwayDirection.getGeom().getCoordinate().distance(i) > runwayDirection.getGeom().getCoordinate().distance(j))
                        return 1;
                    else
                        return -1;
                })
                .collect(Collectors.toList());

        double azimuth = runwayDirection.getAzimuth() + 180;

        Coordinate extreme1= move(extremes.get(2), azimuth+90, runwayDirection.getTakeoffSection().getClearwayWidth());
        Coordinate extreme4= move(extremes.get(3), azimuth-90, runwayDirection.getTakeoffSection().getClearwayWidth());
        Coordinate extreme2= move(extreme1, azimuth, -1*runwayDirection.getTakeoffSection().getClearwayLength());
        Coordinate extreme3= move(extreme4, azimuth, -1*runwayDirection.getTakeoffSection().getClearwayLength());

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
