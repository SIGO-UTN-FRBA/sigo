package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayApproachSection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.*;
import org.geotools.referencing.GeodeticCalculator;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class RunwayApproachSectionService extends SigoService<RunwayApproachSection, RunwayDirection> {

    @Inject
    public RunwayApproachSectionService(HibernateUtil hibernateUtil) {
        super(RunwayApproachSection.class, hibernateUtil.getSessionFactory());
    }

    public Polygon getThresholdGeometry(RunwayDirection direction) {

        final Coordinate[] coordinates = direction.getRunway().getGeom().getCoordinates();

        List<Coordinate> collect = Arrays.stream(coordinates)
                .distinct()
                .sorted((i, j) -> {
                    if (direction.getGeom().getCoordinate().distance(i) > direction.getGeom().getCoordinate().distance(j))
                        return 1;
                    else
                        return -1;
                })
                .limit(2)
                .collect(Collectors.toList());

        GeodeticCalculator calc1 = new GeodeticCalculator();
        GeodeticCalculator calc2 = new GeodeticCalculator();

        calc1.setStartingGeographicPoint(collect.get(0).x,collect.get(0).y);
        calc2.setStartingGeographicPoint(collect.get(1).x,collect.get(1).y);

        calc1.setDestinationGeographicPoint(collect.get(1).x,collect.get(1).y);

        double azimuth = calc1.getAzimuth();
        calc1.setDirection(azimuth + 90, 250);
        calc2.setDirection(azimuth + 90, 250);

        Coordinate dest1 = new Coordinate();
        Coordinate dest2 = new Coordinate();

        dest1.x = calc1.getDestinationGeographicPoint().getX();

        dest1.y = calc1.getDestinationGeographicPoint().getY();

        dest2.x = calc2.getDestinationGeographicPoint().getX();

        dest2.y = calc2.getDestinationGeographicPoint().getY();

        return new GeometryFactory().createPolygon(new Coordinate[]{collect.get(0), dest1, dest2,collect.get(1),collect.get(0)});

    }

}
