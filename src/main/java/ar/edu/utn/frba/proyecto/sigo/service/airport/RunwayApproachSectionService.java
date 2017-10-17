package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayApproachSection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.getAzimuth;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.getMiddle;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.move;

@Singleton
public class RunwayApproachSectionService extends SigoService<RunwayApproachSection, RunwayDirection> {

    @Inject
    public RunwayApproachSectionService(HibernateUtil hibernateUtil) {
        super(RunwayApproachSection.class, hibernateUtil.getSessionFactory());
    }

    public Geometry getThresholdGeometry(RunwayDirection runwayDirection) {

        List<Coordinate> extremes = sortDirectionCoordinates(runwayDirection.getRunway().getGeom().getCoordinates(), runwayDirection);

        Coordinate extreme1 = extremes.get(0);
        Coordinate extreme4 = extremes.get(1);
        double azimuth = getAzimuthRunway(runwayDirection);

        Coordinate extreme2 = move(extreme1, azimuth, -1 * runwayDirection.getApproachSection().getThresholdLength());
        Coordinate extreme3 = move(extreme4, azimuth, -1 * runwayDirection.getApproachSection().getThresholdLength());

        //return new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
        return getRunwayStrip(runwayDirection);
    }

    private List<Coordinate> sortDirectionCoordinates(Coordinate[] unsortedCoordinates, RunwayDirection runwayDirection){
        List<Coordinate> sortedCoordinates = Arrays.stream(unsortedCoordinates)
                .distinct()
                .sorted((i, j) -> {
                    if (runwayDirection.getGeom().getCoordinate().distance(i) > runwayDirection.getGeom().getCoordinate().distance(j))
                        return 1;
                    else
                        return -1;
                })
                .limit(4)
                .collect(Collectors.toList());
        return sortedCoordinates;
    }

    /*Determino los puntos de extremo del centro de la pista en base a la direccion de la pista*/
    private Coordinate[] getCenterRunwayPoint(RunwayDirection runwayDirection){
        List<Coordinate> extremes = sortDirectionCoordinates(runwayDirection.getRunway().getGeom().getCoordinates(), runwayDirection);
        Coordinate startCenterPoint = getMiddle(extremes.get(0), extremes.get(1));
        Coordinate endCenterPoint = getMiddle(extremes.get(2), extremes.get(3));
        return new Coordinate[]{startCenterPoint, endCenterPoint};
    }

    /*Obtengo el azimuth de la pista en base a sus puntos centrales*/
    private double getAzimuthRunway(RunwayDirection runwayDirection){
        Coordinate[] centerRunwayPath = getCenterRunwayPoint(runwayDirection);
        return getAzimuth(centerRunwayPath[0], centerRunwayPath[1]);
    }

    /*Obtengo un objeto LineString en base a sus puntos centrales*/
    private LineString getCenterRunwayPath(RunwayDirection runwayDirection){
        Coordinate[] centerRunwayPath = getCenterRunwayPoint(runwayDirection);
        return new GeometryFactory().createLineString(new Coordinate[]{centerRunwayPath[0], centerRunwayPath[1]});
    }

    private Geometry getRunwayStrip(RunwayDirection runwayDirection){
        List<Coordinate> extremes = sortDirectionCoordinates(runwayDirection.getRunway().getGeom().getCoordinates(), runwayDirection);

        LineString extremeLine1 = new GeometryFactory().createLineString(new Coordinate[]{extremes.get(0), extremes.get(1)});
        LineString extremeLine2 = new GeometryFactory().createLineString(new Coordinate[]{extremes.get(2), extremes.get(3)});
        LineString extremeLine3 = new GeometryFactory().createLineString(new Coordinate[]{extremes.get(1), extremes.get(2)});
        LineString extremeLine4 = new GeometryFactory().createLineString(new Coordinate[]{extremes.get(3), extremes.get(0)});
        return extremeLine1.buffer(0.001, 16).union(extremeLine2.buffer(0.001,16).union(extremeLine3.buffer(0.001)).union(extremeLine4.buffer(0.001)));
    }

}
