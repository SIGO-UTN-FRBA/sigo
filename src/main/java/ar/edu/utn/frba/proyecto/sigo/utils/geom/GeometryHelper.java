package ar.edu.utn.frba.proyecto.sigo.utils.geom;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.GeodeticCalculator;

import javax.inject.Singleton;
import java.awt.geom.Point2D;

public class GeometryHelper {

    public static double azimuth(Coordinate a, Coordinate b) {

        GeodeticCalculator gc = new GeodeticCalculator();

        gc.setStartingGeographicPoint(a.x, a.y);
        gc.setDestinationGeographicPoint(b.x, b.y);

        return gc.getAzimuth();
    }

    public static Coordinate move(Coordinate coordinate, double azimuth, double distance){

        GeodeticCalculator gc = new GeodeticCalculator();

        gc.setStartingGeographicPoint(coordinate.x, coordinate.y);
        gc.setDirection(azimuth, distance);

        Point2D destination = gc.getDestinationGeographicPoint();

        return new Coordinate(destination.getX(), destination.getY());
    }

    public static Double distanceInMeters(Coordinate start, Coordinate end){

        GeodeticCalculator gc = new GeodeticCalculator();

        gc.setStartingGeographicPoint(start.x, start.y);
        gc.setDestinationGeographicPoint(end.x, end.y);

        return gc.getOrthodromicDistance();
    }

}
