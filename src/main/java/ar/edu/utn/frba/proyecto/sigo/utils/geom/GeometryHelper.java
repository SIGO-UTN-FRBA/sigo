package ar.edu.utn.frba.proyecto.sigo.utils.geom;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.GeodeticCalculator;

import javax.inject.Singleton;
import java.awt.geom.Point2D;

@Singleton
public class GeometryHelper {

    public static double getAzimuth(Coordinate a, Coordinate b) {

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

    public static int getDirection(double azimuth){
        if((azimuth > 0D && azimuth < 90D) || (azimuth > 270 && azimuth < 360))
            return 1;
        else
            return -1;
    }
}
