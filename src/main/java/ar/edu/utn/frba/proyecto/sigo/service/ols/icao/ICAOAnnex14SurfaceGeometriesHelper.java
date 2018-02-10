package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.*;
import ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.buffer.BufferParameters;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper.*;

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

    public Polygon createConicalSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceConical conicalDefinition, ICAOAnnex14SurfaceInnerHorizontal innerHorizontalSurface) {

        Geometry baseGeometry = innerHorizontalSurface.getGeometry();

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
        return (Polygon) difference.getGeometryN(0);
    }

    public Polygon createApproachFirstSectionSurfaceGeometry(RunwayDirection direction, ICAOAnnex14SurfaceApproach approach, ICAOAnnex14SurfaceApproachFirstSection approachFirstSection, ICAOAnnex14SurfaceStrip strip){

        Polygon approachGeometry;
        Coordinate threshold;
        Coordinate extreme1;
        Coordinate extreme2;
        Coordinate extreme3;
        Coordinate extreme4;
        Double azimuth;
        Double divergence;

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
        Double divergence;

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

    public Polygon createApproachHorizontalSectionSurfaceGeometry(
            RunwayDirection direction,
            ICAOAnnex14SurfaceApproachHorizontalSection approachHorizontalSection,
            ICAOAnnex14SurfaceApproachSecondSection approachSecondSection,
            ICAOAnnex14SurfaceApproachFirstSection approachFirstSection,
            ICAOAnnex14SurfaceApproach approach
    ) {

        Polygon approachGeometry;
        Coordinate threshold;
        Coordinate extreme1;
        Coordinate extreme2;
        Coordinate extreme3;
        Coordinate extreme4;
        Double azimuth;
        Double divergence;
        Double innerEdgeShift;
        Double outerEdgeShift;

        divergence = slopePercentToDegrees(approach.getDivergence());

        azimuth = realAzimuth(direction);

        threshold = direction.getGeom().getCoordinate();

        innerEdgeShift = approachFirstSection.getLength() + approachSecondSection.getLength();
        outerEdgeShift = approachHorizontalSection.getLength();

        //1. inner edge
        extreme1 = move(threshold, azimuth, -1 * approach.getDistanceFromThreshold());
        extreme1 = move(extreme1, azimuth-90, -1 * approach.getLengthOfInnerEdge()/2);
        extreme1 = move(extreme1, azimuth-divergence,-1 * innerEdgeShift);

        extreme2 = move(threshold, azimuth, -1 * approach.getDistanceFromThreshold());
        extreme2 = move(extreme2, azimuth+90, -1 * approach.getLengthOfInnerEdge()/2);
        extreme2 = move(extreme2, azimuth+divergence, -1 * innerEdgeShift);

        //2. outer edge
        extreme3 = move(extreme2, azimuth+divergence, -1 * outerEdgeShift);
        extreme4 = move(extreme1, azimuth-divergence, -1 * outerEdgeShift);

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

        Double azimuth;
        Double divergence;
        Double shiftInitialLength;
        Double lenghtBeforeBreak;
        Double lenghtAfterBreak;
        Double shiftInnerEdgeWidth;
        Double fixedWidth;
        Coordinate runwayExtreme;
        Coordinate extreme1;
        Coordinate extreme4;
        Coordinate extreme2;
        Coordinate extreme3;
        Coordinate break1;
        Coordinate break2;
        Polygon takeoffGeometry;

        shiftInitialLength = Math.max(direction.getTakeoffSection().getClearwayLength(), takeoffClimb.getDistanceFromRunwayEnds());

        azimuth = realAzimuth(direction);

        runwayExtreme = oppositeThreshold(direction);

        //1. inner edge
        shiftInnerEdgeWidth = takeoffClimb.getLengthOfInnerEdge()/2;

        extreme1 = move(runwayExtreme, azimuth, shiftInitialLength);
        extreme1 = move(extreme1,azimuth+90, shiftInnerEdgeWidth);

        extreme2 = move(runwayExtreme,azimuth, shiftInitialLength);
        extreme2 = move(extreme2,azimuth-90, shiftInnerEdgeWidth);

        //2. break points
        Double opposite = takeoffClimb.getFinalWidth() / 2;
        lenghtBeforeBreak = opposite / Math.tan(takeoffClimb.getDivergence() / 100);
        lenghtAfterBreak = takeoffClimb.getLength() - lenghtBeforeBreak;

        divergence = slopePercentToDegrees(takeoffClimb.getDivergence());

        break1 = move(extreme2,azimuth-divergence, lenghtBeforeBreak);
        break2 = move(extreme1,azimuth+divergence, lenghtBeforeBreak);

        //3. outer edge
        extreme3 = move(break1,azimuth, lenghtAfterBreak);
        extreme4 = move(break2,azimuth, lenghtAfterBreak);

        //4. create polygon
        takeoffGeometry = new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, break1, extreme3, extreme4, break2, extreme1});
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

    public Polygon createBalkedLandingSurfaceGeometry(
            RunwayDirection direction,
            ICAOAnnex14SurfaceBalkedLanding balkedLanding,
            ICAOAnnex14SurfaceInnerHorizontal innerHorizontal) {

        Double azimuth;
        Double divergenceDegrees;
        Double shiftLength, shiftWidth;
        Coordinate threshold;
        Coordinate extreme1;
        Coordinate extreme4;
        Coordinate extreme2;
        Coordinate extreme3;
        Polygon takeoffGeometry;

        azimuth = realAzimuth(direction);

        shiftWidth = balkedLanding.getLengthOfInnerEdge()/2;

        shiftLength = balkedLanding.getDistanceFromThreshold();

        threshold = direction.getGeom().getCoordinate();

        //1. inner edge
        extreme1 = move(threshold ,azimuth, shiftLength);
        extreme1 = move(extreme1,azimuth+90, shiftWidth);

        extreme2 = move(threshold,azimuth, shiftLength);
        extreme2 = move(extreme2,azimuth-90, shiftWidth);

        //2. outer edge
        divergenceDegrees = slopePercentToDegrees(balkedLanding.getDivergence());

        Double shiftOuterEdgeWidth = innerHorizontal.getHeight() / Math.tan(balkedLanding.getSlope() / 100);

        extreme3 = move(extreme2,azimuth-divergenceDegrees, shiftOuterEdgeWidth);
        extreme4 = move(extreme1,azimuth+divergenceDegrees, shiftOuterEdgeWidth);

        //3. create polygon
        takeoffGeometry = new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});

        return takeoffGeometry;
    }

    public Polygon createOuterHorizontalSurfaceGeometry(
            RunwayDirection direction,
            ICAOAnnex14SurfaceOuterHorizontal outerHorizontal,
            ICAOAnnex14SurfaceConical conical
    ) {
        Point arp = direction.getRunway().getAirport().getGeom();

        Geometry fullBuffer = arp.buffer(outerHorizontal.getRadius() / 100000, 30);

        return (Polygon) fullBuffer.difference(conical.getGeometry()).getGeometryN(0);
    }
}
