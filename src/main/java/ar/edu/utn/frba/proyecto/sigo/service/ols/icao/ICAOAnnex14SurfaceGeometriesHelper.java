package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproach;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachFirstSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachSecondSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceTakeoffClimb;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceTransitional;
import ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.buffer.BufferParameters;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper.move;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper.oppositeThreshold;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper.realAzimuth;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper.slopePercentToDegrees;

@Singleton
public class ICAOAnnex14SurfaceGeometriesHelper {

    public Polygon createStripSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceStrip stripDefinition) {

        Double extraRunwayWidth, extraRunwayLength, azimuth;

        extraRunwayWidth = Optional.ofNullable(direction.getStrip().getWidth())
                    .map(w -> w/2)
                    .orElse(stripDefinition.getWidth());

        extraRunwayLength = Optional.ofNullable(direction.getStrip().getLength())
                .map( l -> (l - direction.getRunway().getLength())/2)
                .orElse(stripDefinition.getLength());

        azimuth = realAzimuth(direction);

        Coordinate threshold = direction.getGeom().getCoordinate();
        Coordinate oppositeThreshold = oppositeThreshold(direction);

        Coordinate extreme1 = GeographicHelper.move(threshold, azimuth, -1 * extraRunwayLength);
        extreme1 = GeographicHelper.move(extreme1, azimuth+90, extraRunwayWidth);

        Coordinate extreme2 = GeographicHelper.move(threshold, azimuth, -1 * extraRunwayLength);
        extreme2 = GeographicHelper.move(extreme2, azimuth-90, extraRunwayWidth);

        Coordinate extreme3 = GeographicHelper.move(oppositeThreshold, azimuth, extraRunwayLength);
        extreme3 = GeographicHelper.move(extreme3, azimuth-90, extraRunwayWidth);

        Coordinate extreme4 = GeographicHelper.move(oppositeThreshold, azimuth, extraRunwayLength);
        extreme4 = GeographicHelper.move(extreme4, azimuth+90, extraRunwayWidth);

        Polygon stripGeometry = new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON(14).write(stripGeometry, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.toString();
*/
        return stripGeometry;
    }

    public Polygon createInnerHorizontalSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceInnerHorizontal innerHorizontalDefinition, ICAOAnnex14SurfaceStrip stripSurface) {

        double radius = innerHorizontalDefinition.getRadius() / 100000;

        LineString runwayGeometry = direction.getRunway().getGeom();

        List<Point> directionPoints = Lists.newArrayList(runwayGeometry.getStartPoint(), runwayGeometry.getEndPoint());

        Geometry bufferCenterLine = runwayGeometry.buffer(radius,20);

        List<Geometry> bufferEdgePoints = directionPoints.stream().map(d -> d.buffer(radius,20)).collect(Collectors.toList());

        Geometry union = bufferCenterLine.union(bufferEdgePoints.get(0)).union(bufferEdgePoints.get(1));

        Geometry difference = union.difference(stripSurface.getGeometry());
/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON(14).write(union, out);
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
            new GeometryJSON(14).write(difference, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.toString();
*/
        return (Polygon) difference;
    }

    public Polygon createApproachFirstSectionSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceApproach approach, ICAOAnnex14SurfaceApproachFirstSection approachFirstSection, ICAOAnnex14SurfaceStrip strip){

        Polygon approachGeometry;
        Coordinate threshold;
        Coordinate extreme1;
        Coordinate extreme2;
        Coordinate extreme3;
        Coordinate extreme4;
        Double azimuth;
        double divergence;

        divergence = slopePercentToDegrees(approach.getDivergence());

        azimuth = realAzimuth(direction);

        threshold = direction.getGeom().getCoordinate();

        //1. inner edge
        extreme1 = move(threshold, azimuth, -1 * approach.getDistanceFromThreshold());
        extreme1 = move(extreme1, azimuth-90, approach.getLengthOfInnerEdge()/2);

        extreme2 = move(threshold, azimuth, -1 * approach.getDistanceFromThreshold());
        extreme2 = move(extreme2, azimuth+90, approach.getLengthOfInnerEdge()/2);

        //2. outer edge
        extreme3 = move(extreme2, azimuth-divergence, -1 * approachFirstSection.getLength());
        extreme4 = move(extreme1, azimuth+divergence, -1 * approachFirstSection.getLength());

        //3. create polygon
        approachGeometry = new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON(14).write(approachGeometry, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.toString();
*/
        return approachGeometry;
    }

    public Polygon createApproachSecondSectionSurfaceGeometry(
            RunwayDirection direction,
            ICAOAnnex14SurfaceApproachSecondSection approachSecondSection,
            ICAOAnnex14SurfaceApproach approach,
            ICAOAnnex14SurfaceApproachFirstSection approachFirstSection) {

        Polygon approachGeometry;
        Coordinate threshold;
        Coordinate extreme1;
        Coordinate extreme2;
        Coordinate extreme3;
        Coordinate extreme4;
        Double azimuth;
        double divergence;

        divergence = slopePercentToDegrees(approach.getDivergence());

        azimuth = realAzimuth(direction);

        threshold = direction.getGeom().getCoordinate();

        //1. inner edge
        extreme1 = move(threshold, azimuth, -1 * approach.getDistanceFromThreshold());
        extreme1 = move(extreme1, azimuth-90, -1 * approach.getLengthOfInnerEdge()/2);
        extreme1 = move(extreme1, azimuth-divergence,-1 * approachFirstSection.getLength());

        extreme2 = move(threshold, azimuth, -1 * approach.getDistanceFromThreshold());
        extreme2 = move(extreme2, azimuth+90, -1 * approach.getLengthOfInnerEdge()/2);
        extreme2 = move(extreme2, azimuth+divergence, -1 * approachFirstSection.getLength());

        //2. outer edge
        extreme3 = move(extreme2, azimuth+divergence, -1 * approachSecondSection.getLength());
        extreme4 = move(extreme1, azimuth-divergence, -1 * approachSecondSection.getLength());

        //3. create polygon
        approachGeometry = new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON(14).write(approachGeometry, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.toString();
*/
        return approachGeometry;
    }

    public MultiPolygon createTransitionalSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceTransitional transitional, ICAOAnnex14SurfaceStrip strip, ICAOAnnex14SurfaceApproachFirstSection approach, ICAOAnnex14SurfaceInnerHorizontal innerHorizontal) {

        MultiPolygon centerPart;

        //1. calcular parte central

        centerPart = (MultiPolygon) direction.getRunway()
                .getGeom()
                .buffer(transitional.getWidth() / 100000, 1, BufferParameters.CAP_FLAT)
                .difference(strip.getGeometry());

        //TODO 2. calcular parte borde extremos

        //3. unir partes
/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON(14).write(centerPart, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.toString();
*/
        return centerPart;
    }

    public Polygon createTakeoffClimbSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceTakeoffClimb takeoffClimb) {

        double azimuth;
        double divergence;
        double shiftLength, shiftWidth;
        Coordinate runwayExtreme;
        Coordinate extreme1;
        Coordinate extreme4;
        Coordinate extreme2;
        Coordinate extreme3;
        Polygon takeoffGeometry;

        shiftLength = Math.max(direction.getTakeoffSection().getClearwayLength(), takeoffClimb.getDistanceFromRunwayEnds());

        azimuth = realAzimuth(direction);

        shiftWidth = takeoffClimb.getLengthOfInnerEdge()/2;

        runwayExtreme = oppositeThreshold(direction);

        //1. inner edge
        extreme1 = move(runwayExtreme,azimuth,shiftLength);
        extreme1 = move(extreme1,azimuth+90, shiftWidth);

        extreme2 = move(runwayExtreme,azimuth,shiftLength);
        extreme2 = move(extreme2,azimuth-90, shiftWidth);


        //2. outer edge
        divergence = slopePercentToDegrees(takeoffClimb.getDivergence());
        extreme3 = move(extreme2,azimuth-divergence,takeoffClimb.getLength());
        extreme4 = move(extreme1,azimuth+divergence,takeoffClimb.getLength());

        //3. create polygon
        takeoffGeometry = new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});

/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON(14).write(geometry, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.toString();
*/
        return takeoffGeometry;
    }
}
