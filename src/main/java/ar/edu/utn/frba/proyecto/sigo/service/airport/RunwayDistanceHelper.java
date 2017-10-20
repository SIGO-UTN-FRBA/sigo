package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import java.util.List;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.getAzimuth;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.getMiddle;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.sortDirectionCoordinates;

public class RunwayDistanceHelper {

    public static Double calculateTORALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        return runway;
    }

    public static Double calculateTODALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        Double clearway = direction.getTakeoffSection().getClearwayLength();

        return runway +  clearway;
    }

    public static Double calculateASDALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        Double stopway = direction.getTakeoffSection().getStopwayLength();

        return runway + stopway;
    }

    public static Double calculateLDALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        Double threshold = direction.getApproachSection().getThresholdLength();

        return runway - threshold;
    }

    public static Coordinate[] getCenterRunwayPoint(RunwayDirection direction){
        List<Coordinate> extremes = sortDirectionCoordinates(direction.getRunway().getGeom().getCoordinates(), direction.getGeom().getCoordinate());
        Coordinate startCenterPoint = getMiddle(extremes.get(0), extremes.get(1));
        Coordinate endCenterPoint = getMiddle(extremes.get(2), extremes.get(3));
        return new Coordinate[]{startCenterPoint, endCenterPoint};
    }

    /*Obtengo el azimuth de la pista en base a sus puntos centrales*/
    public static double getAzimuthRunway(RunwayDirection direction){
        Coordinate[] centerRunwayPath = getCenterRunwayPoint(direction);
        return getAzimuth(centerRunwayPath[0], centerRunwayPath[1]);
    }

    /*Obtengo un objeto LineString en base a sus puntos centrales*/
    public static LineString getCenterRunwayPath(RunwayDirection direction){
        Coordinate[] centerRunwayPath = getCenterRunwayPoint(direction);
        return new GeometryFactory().createLineString(new Coordinate[]{centerRunwayPath[0], centerRunwayPath[1]});
    }
}
