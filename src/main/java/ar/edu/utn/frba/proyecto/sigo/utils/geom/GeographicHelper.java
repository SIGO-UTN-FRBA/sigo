package ar.edu.utn.frba.proyecto.sigo.utils.geom;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.GeodeticCalculator;

import javax.inject.Singleton;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;

public class GeographicHelper {

    //TODO estos metodos q tienen algo de dominio deberian estar fuera creo
    public static Coordinate oppositeThreshold(RunwayDirection direction){
        Point point0 = direction.getRunway().getGeom().getPointN(0);
        Point point1 = direction.getRunway().getGeom().getPointN(1);

        //no siempre puede coincidir, por eso calculo distancia
        if (point0.distance(direction.getGeom()) < point1.distance(direction.getGeom()) )
            return point1.getCoordinate();
        else
            return point0.getCoordinate();
    }

    public static Double realAzimuth(RunwayDirection direction) {

        LineString runwayGeometry = direction.getRunway().getGeom();

        double azimuth = azimuth(runwayGeometry.getStartPoint().getCoordinate(), runwayGeometry.getEndPoint().getCoordinate());

        double oppositeAzimuth = azimuth(runwayGeometry.getEndPoint().getCoordinate(), runwayGeometry.getStartPoint().getCoordinate());

        if(direction.getNumber() < 18)
            return (azimuth > 0) ? azimuth : oppositeAzimuth;
        else
            return 360 + ((azimuth < 0) ? azimuth : oppositeAzimuth);
    }

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

    public static Double slopePercentToDegrees(Double percent){
        return Math.toDegrees(Math.atan(percent/100));
    }

}
