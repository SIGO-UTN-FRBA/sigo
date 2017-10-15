package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayApproachSection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.getDirection;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.move;

@Singleton
public class RunwayApproachSectionService extends SigoService<RunwayApproachSection, RunwayDirection> {

    @Inject
    public RunwayApproachSectionService(HibernateUtil hibernateUtil) {
        super(RunwayApproachSection.class, hibernateUtil.getSessionFactory());
    }

    public Polygon getThresholdGeometry(RunwayDirection runwayDirection) {

        final Coordinate[] runwayCoordinates = runwayDirection.getRunway().getGeom().getCoordinates();

        List<Coordinate> extremes = Arrays.stream(runwayCoordinates)
                .distinct()
                .sorted((i, j) -> {
                    if (runwayDirection.getGeom().getCoordinate().distance(i) > runwayDirection.getGeom().getCoordinate().distance(j))
                        return 1;
                    else
                        return -1;
                })
                .limit(2)
                .collect(Collectors.toList());

        double azimuth = runwayDirection.getNumber()*10;
        int direction = getDirection(azimuth);

        Coordinate extreme1 = extremes.get(0);
        Coordinate extreme4 = extremes.get(1);
        Coordinate extreme2 = move(extreme1, azimuth, direction * runwayDirection.getApproachSection().getThresholdLength());
        Coordinate extreme3 = move(extreme4, azimuth, direction * runwayDirection.getApproachSection().getThresholdLength());

        return new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
    }

}
