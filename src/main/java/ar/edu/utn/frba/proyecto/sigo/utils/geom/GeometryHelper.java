package ar.edu.utn.frba.proyecto.sigo.utils.geom;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.GeodeticCalculator;

import javax.inject.Singleton;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static Coordinate getMiddle(Coordinate startPoint, Coordinate finalPoint){
        LineSegment segment = new LineSegment(startPoint, finalPoint);
        return segment.pointAlong(0.5);
    }


    public static List<Coordinate> sortDirectionCoordinates(Coordinate[] unsortedCoordinates, Coordinate referenceCoordinate){
        List<Coordinate> sortedCoordinates = Arrays.stream(unsortedCoordinates)
                .distinct()
                .sorted((i, j) -> {
                    if (referenceCoordinate.distance(i) > referenceCoordinate.distance(j))
                        return 1;
                    else
                        return -1;
                })
                .limit(4)
                .collect(Collectors.toList());
        return sortedCoordinates;
    }

}
