package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayTakeoffSection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDistanceHelper.getAzimuthRunway;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.move;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.sortDirectionCoordinates;

@Singleton
public class RunwayTakeoffSectionService extends SigoService<RunwayTakeoffSection, RunwayDirection> {

    @Inject
    public RunwayTakeoffSectionService(HibernateUtil hibernateUtil) {
        super(RunwayTakeoffSection.class, hibernateUtil.getSessionFactory());
    }

    public Geometry getStopwayGeometry(RunwayDirection direction) {

        List<Coordinate> extremes = sortDirectionCoordinates(direction.getRunway().getGeom().getCoordinates(), direction.getGeom().getCoordinate());
        Coordinate extreme2= move(extremes.get(2), getAzimuthRunway(direction), direction.getTakeoffSection().getStopwayLength());
        Coordinate extreme3= move(extremes.get(3), getAzimuthRunway(direction), direction.getTakeoffSection().getStopwayLength());

        return new GeometryFactory().createPolygon(new Coordinate[]{extremes.get(2), extreme2, extreme3, extremes.get(3), extremes.get(2)});
    }

    public Geometry getClearwayGeometry(RunwayDirection direction) {

        List<Coordinate> extremes = sortDirectionCoordinates(direction.getRunway().getGeom().getCoordinates(), direction.getGeom().getCoordinate());
        Coordinate extreme1= move(extremes.get(2), getAzimuthRunway(direction)+90, direction.getTakeoffSection().getClearwayWidth());
        Coordinate extreme4= move(extremes.get(3), getAzimuthRunway(direction)-90, direction.getTakeoffSection().getClearwayWidth());
        Coordinate extreme2= move(extreme1, getAzimuthRunway(direction), direction.getTakeoffSection().getClearwayLength());
        Coordinate extreme3= move(extreme4, getAzimuthRunway(direction), direction.getTakeoffSection().getClearwayLength());

        return new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
    }
}
