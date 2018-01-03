package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayApproachSection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.getAzimuth;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.move;

@Singleton
public class RunwayApproachSectionService extends SigoService<RunwayApproachSection, RunwayDirection> {

    @Inject
    public RunwayApproachSectionService(HibernateUtil hibernateUtil) {
        super(RunwayApproachSection.class, hibernateUtil.getSessionFactory());
    }

    public SimpleFeature getThresholdFeature(RunwayDirection runwayDirection) {

        return SimpleFeatureBuilder.build(
                getThresholdFeatureSchema(),
                new Object[]{
                        calculateThresholdGeometry(runwayDirection),
                        "Threshold Displace",
                        runwayDirection.getApproachSection().getThresholdLength()
                },
                runwayDirection.getApproachSection().getId().toString()
        );
    }

    private Geometry calculateThresholdGeometry(RunwayDirection runwayDirection) {

        double azimuth = getAzimuth(runwayDirection.getStartPoint().getCoordinate(), runwayDirection.getEndPoint().getCoordinate());
        double azimuthInv = getAzimuth(runwayDirection.getEndPoint().getCoordinate(), runwayDirection.getStartPoint().getCoordinate());

        Coordinate extreme1 = move(runwayDirection.getStartPoint().getCoordinate(),azimuth + 90, runwayDirection.getRunway().getWidth()/2);
        Coordinate extreme2 = move(runwayDirection.getStartPoint().getCoordinate(),azimuth - 90, runwayDirection.getRunway().getWidth()/2);
        Coordinate extreme3 = move(extreme2,azimuthInv, runwayDirection.getApproachSection().getThresholdLength());
        Coordinate extreme4 = move(extreme1,azimuthInv, runwayDirection.getApproachSection().getThresholdLength());


        return new GeometryFactory().createPolygon(new Coordinate[]{extreme1, extreme2, extreme3, extreme4, extreme1});
    }

    private SimpleFeatureType getThresholdFeatureSchema() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Threshold Displaced");
        tb.add("geom", java.awt.Polygon.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("length", Double.class);

        return tb.buildFeatureType();
    }

}
