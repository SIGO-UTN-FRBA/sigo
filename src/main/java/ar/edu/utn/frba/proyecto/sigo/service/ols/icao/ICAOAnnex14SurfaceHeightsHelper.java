package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.*;
import com.vividsolutions.jts.geom.*;

import javax.inject.Singleton;

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
                return this.determineHeightAt((ICAOAnnex14SurfaceBalkedLanding) surface, point);
            case TAKEOFF_CLIMB:
                return this.determineHeightAt((ICAOAnnex14SurfaceTakeoffClimb) surface, point);
            case OUTER_HORIZONTAL:
                return this.determineHeightAt((ICAOAnnex14SurfaceOuterHorizontal) surface, point);
        }

        return -1D; //TODO completar calculos de altura para todas las superficies
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceConical surface, Point point){

        return surface.getInitialHeight() + calculateHeightByTrigonometry(
                surface.getGeometry().getInteriorRingN(0),
                surface.getSlope(),
                point
        );
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceStrip surface, Point point){
        return surface.getInitialHeight() + 0D;
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceInnerHorizontal surface, Point point){
        return surface.getInitialHeight() + surface.getHeight();
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceTransitional surface, Point point){

        return surface.getInitialHeight() + (100 /surface.getSlope()) * surface.getWidth(); //TODO
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

    public Double determineHeightAt(ICAOAnnex14SurfaceOuterHorizontal surface, Point point){
        return surface.getInitialHeight() +surface.getHeight();
    }

    public Double determineHeightAt(ICAOAnnex14SurfaceBalkedLanding surface, Point point){
        return surface.getInitialHeight() + calculateHeightForSlopingPolygon(surface.getGeometry(), surface.getSlope(), point);
    }


    public Double calculateHeightForSlopingPolygon(Polygon geometry, Double slope, Point point){

        LineString exteriorRing = geometry.getExteriorRing();

        return calculateHeightByTrigonometry(
                new GeometryFactory().createLineString(
                        //FIXME esta atado a como construyo la geom
                        new Coordinate[]{
                                exteriorRing.getPointN(0).getCoordinate(),
                                exteriorRing.getPointN(1).getCoordinate()
                        }
                ),
                slope,
                point
        );
    }

    public Double calculateHeightByTrigonometry(Geometry geometry, Double slope, Point point){

        Double adjacent = geometry.distance(point) * 100000; //TODO distancia geografica

        return adjacent * Math.tan(slope / 100);
    }
}
