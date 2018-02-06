package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.*;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import com.vividsolutions.jts.geom.*;

import javax.inject.Singleton;
import java.util.stream.IntStream;

@Singleton
public class ICAOAnnex14SurfaceHeightsHelper {

    public Double determineHeightAt(ICAOAnnex14Surface surface, Point point){

        switch (surface.getEnum()) {
            case STRIP:
                return this.determineHeightAt((ICAOAnnex14SurfaceStrip) surface, point);
            case CONICAL:
                return this.determineHeightAt((ICAOAnnex14SurfaceConical) surface, point);
            case INNER_HORIZONTAL:
                return this.determineHeightAt((ICAOAnnex14SurfaceInnerHorizontal) surface, point);
            case INNER_APPROACH:
                break;
            case APPROACH:
                break;
            case APPROACH_FIRST_SECTION:
                return this.determineHeightAt((ICAOAnnex14SurfaceApproachFirstSection) surface, point);
            case APPROACH_SECOND_SECTION:
                return this.determineHeightAt((ICAOAnnex14SurfaceApproachSecondSection) surface, point);
            case APPROACH_HORIZONTAL_SECTION:
                return this.determineHeightAt((ICAOAnnex14SurfaceApproachHorizontalSection) surface, point);
            case TRANSITIONAL:
                return this.determineHeightAt((ICAOAnnex14SurfaceTransitional) surface, point);
            case INNER_TRANSITIONAL:
                break;
            case BALKED_LANDING_SURFACE:
                break;
            case TAKEOFF_CLIMB:
                return this.determineHeightAt((ICAOAnnex14SurfaceTakeoffClimb) surface, point);
        }

        return -1D; //TODO completar calculos de altura para todas las superficies
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceConical surface, Point point){

        return surface.getInitialHeight() + calculateHeightByPythagoras(
                surface.getGeometry().getInteriorRingN(0),
                surface.getSlope(),
                point
        );
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceStrip surface, Point point){
        return 0D;
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceInnerHorizontal surface, Point point){
        return surface.getHeight();
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceTransitional surface, Point point){

        return IntStream.rangeClosed(0, surface.getGeometry().getNumGeometries())
                .boxed()
                .map( i -> surface.getGeometry().getGeometryN(i))
                .filter( g -> g.intersects(point))
                .findAny()
                .map(g -> surface.getInitialHeight() + calculateHeightByPythagoras(g, surface.getSlope(), point))
                .orElseThrow(()-> new SigoException("No geometry covers the object"));
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceApproachFirstSection surface, Point point){
        return surface.getInitialHeight() + calculateHeightForSlopingPolygon(surface.getGeometry(), surface.getSlope(), point);
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceApproachSecondSection surface, Point point){
        //TODO contemplar tope porque despues de eso es horizontal
        return surface.getInitialHeight() + calculateHeightForSlopingPolygon(surface.getGeometry(), surface.getSlope(), point);
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceApproachHorizontalSection surface, Point point){
        return surface.getInitialHeight();
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceTakeoffClimb surface, Point point){
        return surface.getInitialHeight() + calculateHeightForSlopingPolygon(surface.getGeometry(), surface.getSlope(), point);
    }

    public Double calculateHeightForSlopingPolygon(Polygon geometry, Double slope, Point point){

        LineString exteriorRing = geometry.getExteriorRing();

        return calculateHeightByPythagoras(
                new GeometryFactory().createLineString(
                        new Coordinate[]{
                                exteriorRing.getPointN(0).getCoordinate(),
                                exteriorRing.getPointN(3).getCoordinate()
                        }
                ),
                slope,
                point
        );
    }

    public Double calculateHeightByPythagoras(Geometry geometry, Double slope, Point point){

        double adjacent = geometry.distance(point) * 100000; //TODO distancia geografica

        double degrees = Math.toDegrees(Math.atan(slope / 100));

        double hypotenuse = adjacent / Math.cos(degrees);

        return Math.sqrt(Math.pow(hypotenuse,2) - Math.pow(adjacent,2));
    }
}
