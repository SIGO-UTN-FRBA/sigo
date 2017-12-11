package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surfaces;
import ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class ICAOAnnex14SurfaceGeometriesHelper {

    public Geometry createSurface(RunwayDirection direction, AnalysisSurface currentAnalysisSurface, List<AnalysisSurface> analysisSurfaces) {

        ICAOAnnex14Surface surface = (ICAOAnnex14Surface) currentAnalysisSurface.getSurface();

        switch (ICAOAnnex14Surfaces.values()[surface.getId().intValue()]) {
            case STRIP:
                return createStripSurface(direction, currentAnalysisSurface, analysisSurfaces);
            case CONICAL:
                break;
            case INNER_HORIZONTAL:
                break;
            case INNER_APPROACH:
                break;
            case APPROACH:
                break;
            case APPROACH_FIRST_SECTION:
                break;
            case APPROACH_SECOND_SECTION:
                break;
            case APPROACH_HORIZONTAL_SECTION:
                break;
            case TRANSITIONAL:
                break;
            case INNER_TRANSITIONAL:
                break;
            case BALKED_LANDING_SURFACE:
                break;
            case TAKEOFF_CLIMB:
                break;
        }

        return null;
    }

    private Geometry createStripSurface(RunwayDirection direction, AnalysisSurface currentAnalysisSurface, List<AnalysisSurface> analysisSurfaces) {

        Double extraWidth, extraLength;

        ICAOAnnex14SurfaceStrip stripDefinition = (ICAOAnnex14SurfaceStrip) currentAnalysisSurface.getSurface();

        //1. initialize values
        double halfWidth = direction.getRunway().getWidth() / 2;

        extraWidth = Optional.ofNullable(direction.getStrip().getWidth())
                    .map(w -> w/2 - halfWidth)
                    .orElse(stripDefinition.getWidth() - halfWidth);

        extraLength = Optional.ofNullable(direction.getStrip().getLength())
                    .map(l -> (l - direction.getRunway().getLength())/2)
                    .orElse(stripDefinition.getLength());

        //2. create geom
        Coordinate[] baseCoordinates = direction.getRunway().getGeom().getExteriorRing().norm().getCoordinates();

        double azimuth = GeometryHelper.getAzimuth(baseCoordinates[0], baseCoordinates[3]);

        Coordinate newCoordinate1 = GeometryHelper.move(baseCoordinates[0], azimuth, -extraLength);
        newCoordinate1 = GeometryHelper.move(newCoordinate1, azimuth+90, extraWidth);

        Coordinate newCoordinate2 = GeometryHelper.move(baseCoordinates[1], azimuth, -extraLength);
        newCoordinate2 = GeometryHelper.move(newCoordinate2, azimuth-90, extraWidth);

        Coordinate newCoordinate3 = GeometryHelper.move(baseCoordinates[2], azimuth, extraLength);
        newCoordinate3 = GeometryHelper.move(newCoordinate3, azimuth-90, extraWidth);

        Coordinate newCoordinate4 = GeometryHelper.move(baseCoordinates[3], azimuth, extraLength);
        newCoordinate4 = GeometryHelper.move(newCoordinate4, azimuth+90, extraWidth);

        Polygon stripGeometry = new GeometryFactory().createPolygon(new Coordinate[]{newCoordinate1, newCoordinate2, newCoordinate3, newCoordinate4, newCoordinate1});

/*
        OutputStream out = new ByteArrayOutputStream();
        try {
            new GeometryJSON().write(stripGeometry, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        return stripGeometry;
    }
}
