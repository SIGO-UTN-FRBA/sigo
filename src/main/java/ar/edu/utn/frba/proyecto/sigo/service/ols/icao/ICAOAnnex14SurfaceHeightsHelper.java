package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachFirstSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachSecondSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceTakeoffClimb;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceTransitional;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import com.vividsolutions.jts.geom.*;

import javax.inject.Singleton;
import java.util.stream.IntStream;

@Singleton
public class ICAOAnnex14SurfaceHeightsHelper {

    public Double heightAtCoordinate(ICAOAnnex14Surface surface, Coordinate intersection){

        switch (surface.getEnum()) {
            case STRIP:
                return this.heightAtCoordinate((ICAOAnnex14SurfaceStrip) surface, intersection);
            case CONICAL:
                return this.heightAtCoordinate((ICAOAnnex14SurfaceConical) surface, intersection);
            case INNER_HORIZONTAL:
                return this.heightAtCoordinate((ICAOAnnex14SurfaceInnerHorizontal) surface, intersection);
            case INNER_APPROACH:
                break;
            case APPROACH:
                break;
            case APPROACH_FIRST_SECTION:
                return this.heightAtCoordinate((ICAOAnnex14SurfaceApproachFirstSection) surface, intersection);
            case APPROACH_SECOND_SECTION:
                return this.heightAtCoordinate((ICAOAnnex14SurfaceApproachSecondSection) surface, intersection);
            case APPROACH_HORIZONTAL_SECTION:
                break;
            case TRANSITIONAL:
                return this.heightAtCoordinate((ICAOAnnex14SurfaceTransitional) surface, intersection);
            case INNER_TRANSITIONAL:
                break;
            case BALKED_LANDING_SURFACE:
                break;
            case TAKEOFF_CLIMB:
                return this.heightAtCoordinate((ICAOAnnex14SurfaceTakeoffClimb) surface, intersection);
        }

        return -1D; //TODO completar calculos de altura para todas las superficies
    }


    private Double heightAtCoordinate(ICAOAnnex14SurfaceConical surface, Coordinate intersection){

        return calculateHeightByPythagoras(
                surface.getGeometry().getInteriorRingN(0),
                surface.getSlope(),
                intersection
        );
    }

    private Double heightAtCoordinate(ICAOAnnex14SurfaceStrip surface, Coordinate intersection){
        return 0D;
    }

    private Double heightAtCoordinate(ICAOAnnex14SurfaceInnerHorizontal surface, Coordinate intersection){
        return surface.getHeight();
    }

    private Double heightAtCoordinate(ICAOAnnex14SurfaceTransitional surface, Coordinate intersection){

        Point point = new GeometryFactory().createPoint(intersection);

        return IntStream.rangeClosed(0, surface.getGeometry().getNumGeometries())
                .boxed()
                .map( i -> surface.getGeometry().getGeometryN(i))
                .filter( g -> g.covers(point))
                .findAny()
                .map(g -> calculateHeightByPythagoras(g, surface.getSlope(), intersection))
                .orElseThrow(()-> new SigoException("No geometry covers the object"));
    }

    private Double heightAtCoordinate(ICAOAnnex14SurfaceApproachFirstSection surface, Coordinate intersection){
        return calculateHeightForSlopingPolygon(surface.getGeometry(), surface.getSlope(), intersection);
    }

    private Double heightAtCoordinate(ICAOAnnex14SurfaceApproachSecondSection surface, Coordinate intersection){
        return calculateHeightForSlopingPolygon(surface.getGeometry(), surface.getSlope(), intersection);
    }

    private Double heightAtCoordinate(ICAOAnnex14SurfaceTakeoffClimb surface, Coordinate intersection){
        return calculateHeightForSlopingPolygon(surface.getGeometry(), surface.getSlope(), intersection);
    }

    private Double calculateHeightForSlopingPolygon(Polygon geometry, Double slope, Coordinate intersection){

        LineString exteriorRing = geometry.getExteriorRing();

        return calculateHeightByPythagoras(
                new GeometryFactory().createLineString(
                        new Coordinate[]{
                                exteriorRing.getPointN(0).getCoordinate(),
                                exteriorRing.getPointN(3).getCoordinate()
                        }
                ),
                slope,
                intersection
        );
    }

    private Double calculateHeightByPythagoras(Geometry geometry, Double slope, Coordinate intersection){

        Point point = new GeometryFactory().createPoint(intersection);

        double adjacent = geometry.distance(point) * 100000; //TODO distancia geografica

        double degrees = Math.toDegrees(Math.atan(slope / 100));

        double hypotenuse = adjacent / Math.cos(degrees);

        return Math.sqrt(Math.pow(hypotenuse,2) - Math.pow(adjacent,2));
    }
}
