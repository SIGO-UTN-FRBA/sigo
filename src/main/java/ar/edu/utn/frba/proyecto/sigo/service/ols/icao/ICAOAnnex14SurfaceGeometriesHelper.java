package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class ICAOAnnex14SurfaceGeometriesHelper {

    public Polygon createStripSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceStrip stripDefinition) {

        Double extraWidth, extraLength;

        //1. initialize values
        double halfWidth = direction.getRunway().getWidth() / 2;

        extraWidth = Optional.ofNullable(direction.getStrip().getWidth())
                    .map(w -> w/2 - halfWidth)
                    .orElse(stripDefinition.getWidth() - halfWidth);

        extraLength = Optional.ofNullable(direction.getStrip().getLength())
                    .map(l -> (l - direction.getRunway().getLength())/2)
                    .orElse(stripDefinition.getLength());

        List<Coordinate> directionCoordinates = direction.getRunway().getDirections().stream().map(d -> d.getGeom().getCoordinate()).collect(Collectors.toList());

        double azimuth = GeometryHelper.azimuth(directionCoordinates.get(0), directionCoordinates.get(1));

        //2. create geom

        //TODO incluir en calculo de longitud: 'antes del umbral THR', 'mas alla del extremo o SWY'
        Coordinate[] baseCoordinates = direction.getRunway().getGeom().getExteriorRing().norm().getCoordinates();

        Coordinate newCoordinate1 = GeometryHelper.move(baseCoordinates[0], azimuth, extraLength);
        newCoordinate1 = GeometryHelper.move(newCoordinate1, azimuth+90, extraWidth);

        Coordinate newCoordinate2 = GeometryHelper.move(baseCoordinates[1], azimuth, extraLength);
        newCoordinate2 = GeometryHelper.move(newCoordinate2, azimuth-90, extraWidth);

        Coordinate newCoordinate3 = GeometryHelper.move(baseCoordinates[2], azimuth, -extraLength);
        newCoordinate3 = GeometryHelper.move(newCoordinate3, azimuth-90, extraWidth);

        Coordinate newCoordinate4 = GeometryHelper.move(baseCoordinates[3], azimuth, -extraLength);
        newCoordinate4 = GeometryHelper.move(newCoordinate4, azimuth+90, extraWidth);

        Polygon stripGeometry = new GeometryFactory().createPolygon(new Coordinate[]{newCoordinate1, newCoordinate2, newCoordinate3, newCoordinate4, newCoordinate1});
/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON().write(stripGeometry, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.toString();
*/
        return stripGeometry;
    }

    public Polygon createInnerHorizontalSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceInnerHorizontal innerHorizontalDefinition, ICAOAnnex14SurfaceStrip stripSurface) {

        double radius = innerHorizontalDefinition.getRadius() / 100000;

        List<Point> directionPoints = direction.getRunway().getDirections().stream().map(RunwayDirection::getGeom).collect(Collectors.toList());

        LineString centerLine = new GeometryFactory().createLineString(new Coordinate[]{directionPoints.get(0).getCoordinate(), directionPoints.get(1).getCoordinate()});

        Geometry bufferCenterLine = centerLine.buffer(radius,20);

        List<Geometry> bufferEdgePoints = directionPoints.stream().map(d -> d.buffer(radius,20)).collect(Collectors.toList());

        Geometry union = bufferCenterLine.union(bufferEdgePoints.get(0)).union(bufferEdgePoints.get(1));

        Geometry difference = union.difference(stripSurface.getGeometry());
/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON().write(union, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.toString();
*/
        return (Polygon) difference;
    }

    public Polygon createConicalSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceConical conicalDefinition, ICAOAnnex14SurfaceInnerHorizontal innerHorizontalSurface, ICAOAnnex14SurfaceStrip stripSurface) {

        Geometry baseGeometry = innerHorizontalSurface.getGeometry().union(stripSurface.getGeometry());

        Geometry buffer = baseGeometry.buffer(conicalDefinition.getRatio() / 100000, 25);

        Geometry difference = buffer.difference(baseGeometry);
/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON().write(difference, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.toString();
*/
        return (Polygon) difference;
    }
}
