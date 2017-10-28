package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayTakeoffSection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.move;

@Singleton
public class RunwayTakeoffSectionService extends SigoService<RunwayTakeoffSection, RunwayDirection> {

    @Inject
    public RunwayTakeoffSectionService(HibernateUtil hibernateUtil) {
        super(RunwayTakeoffSection.class, hibernateUtil.getSessionFactory());
    }

    public Polygon getStopwayGeometry(RunwayDirection runwayDirection) {

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

        Coordinate extreme1 = extremes.get(2);
        Coordinate extreme4 = extremes.get(3);
        Coordinate extreme2= move(extreme1, azimuth, -1 *runwayDirection.getTakeoffSection().getStopwayLength());
        Coordinate extreme3= move(extreme4, azimuth, -1 *runwayDirection.getTakeoffSection().getStopwayLength());

        return new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
    }

    public Polygon getClearwayGeometry(RunwayDirection runwayDirection) {

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
}
