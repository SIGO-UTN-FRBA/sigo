package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayApproachSection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
<<<<<<< HEAD
import com.vividsolutions.jts.geom.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
=======
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
>>>>>>> f2185b1b2c52cc0ec7c6eae56107906dc1a73ade
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

import static ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDistanceHelper.*;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.*;

@Singleton
public class RunwayApproachSectionService extends SigoService<RunwayApproachSection, RunwayDirection> {

    @Inject
    public RunwayApproachSectionService(HibernateUtil hibernateUtil) {
        super(RunwayApproachSection.class, hibernateUtil.getSessionFactory());
    }

<<<<<<< HEAD
    public Geometry getThresholdGeometry(RunwayDirection runwayDirection) {
        List<Coordinate> extremes = sortDirectionCoordinates(runwayDirection.getRunway().getGeom().getCoordinates(), runwayDirection.getGeom().getCoordinate());

        double azimuth = getAzimuthRunway(runwayDirection);

        Coordinate extreme2= move(extremes.get(0), azimuth, -1 * runwayDirection.getApproachSection().getThresholdLength());
        Coordinate extreme3= move(extremes.get(1), azimuth, -1 * runwayDirection.getApproachSection().getThresholdLength());

        return new GeometryFactory().createPolygon(new Coordinate[]{extremes.get(0), extreme2, extreme3, extremes.get(1), extremes.get(0)});

    }

    //TODO: migrar el metodo para generar la superficie de Strip
    private Geometry getRunwayStrip(RunwayDirection runwayDirection){
        List<Coordinate> extremes = sortDirectionCoordinates(runwayDirection.getRunway().getGeom().getCoordinates(), runwayDirection.getGeom().getCoordinate());
        LineString extremeLine1 = new GeometryFactory().createLineString(new Coordinate[]{extremes.get(0), extremes.get(1)});
        LineString extremeLine2 = new GeometryFactory().createLineString(new Coordinate[]{extremes.get(2), extremes.get(3)});
        LineString extremeLine3 = new GeometryFactory().createLineString(new Coordinate[]{extremes.get(1), extremes.get(2)});
        LineString extremeLine4 = new GeometryFactory().createLineString(new Coordinate[]{extremes.get(3), extremes.get(0)});
        //Modificar la distancia por la franja que corresponde
        return extremeLine1.buffer(0.001, 16).union(extremeLine2.buffer(0.001,16).union(extremeLine3.buffer(0.001)).union(extremeLine4.buffer(0.001)));
    }

    //TODO: Migrar la superficie de approach
    /*Determino la superficie de approach para la runway*/

    private SimpleFeatureCollection getApproachFeature (RunwayDirection direction){

        Geometry approachGeom = getApproachSurface(direction);
        Double divergence = null;
        Double lenght = null;

        //create the builder
        SimpleFeatureTypeBuilder approachTB = new SimpleFeatureTypeBuilder();

        //add the attributes
        approachTB.setName("Polygon");
        approachTB.add("geom", approachGeom.getClass(), DefaultGeographicCRS.WGS84);
        approachTB.add("name", String.class);
        approachTB.add( "divergence", Double.class );
        approachTB.add( "lenght", Double.class );

        //type of features we would like to build
        SimpleFeatureType schema = approachTB.buildFeatureType();

        //build the feature
        SimpleFeature sf = SimpleFeatureBuilder.build(schema, new Object[] { approachGeom, "Approach Surface", divergence, lenght }, null);

=======
    public SimpleFeature getThresholdFeature(RunwayDirection runwayDirection) {

        return SimpleFeatureBuilder.build(
                getThresholdFeatureSchema(),
                new Object[]{
                        calculateThresholdGeometry(runwayDirection),
                        "Runway",
                        runwayDirection.getApproachSection().getThresholdLength()
                },
                runwayDirection.getApproachSection().getId().toString()
        );
    }

    private Polygon calculateThresholdGeometry(RunwayDirection runwayDirection) {
        final Coordinate[] runwayCoordinates = runwayDirection.getRunway().getGeom().getCoordinates();
>>>>>>> f2185b1b2c52cc0ec7c6eae56107906dc1a73ade

        ListFeatureCollection result = new ListFeatureCollection(schema);
        result.add(sf);

        return result;

    };

    private Geometry getApproachSurface(RunwayDirection runwayDirection){
        Coordinate[] centerRunwayPath = getCenterRunwayPoint(runwayDirection);
        double azimuth = getAzimuthRunway(runwayDirection);
        Coordinate startRightPoint = move(centerRunwayPath[0], azimuth-90,25);
        Coordinate startLeftPoint = move(centerRunwayPath[0], azimuth +90,25);
        //TODO: Modificar 4 por la divergencia
        //TODO: Modificar 500 por la distancia de la superficie de approach
        Coordinate endRightPoint = move(startRightPoint, azimuth+4,-500);
        Coordinate endLeftPoint = move(startLeftPoint, azimuth-4,-500);
        return new GeometryFactory().createPolygon(new Coordinate[]{startRightPoint, endRightPoint, endLeftPoint, startLeftPoint, startRightPoint});
    }

    private SimpleFeatureType getThresholdFeatureSchema() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Threshold Displaced");
        tb.add("geom", java.awt.Polygon.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("length", Double.class);

        return tb.buildFeatureType();
    }

}
