package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayApproachSection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Inject;
import javax.inject.Singleton;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper.azimuth;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeographicHelper.move;

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

    private Polygon calculateThresholdGeometry(RunwayDirection runwayDirection) {

        Coordinate[] extremes = runwayDirection.getRunway().getGeom().norm().getCoordinates();

        double azimuth = azimuth(extremes[0],extremes[3]);

        Coordinate extreme1;
        Coordinate extreme4;
        Coordinate extreme2;
        Coordinate extreme3;

        if(runwayDirection.getNumber()<18){
            extreme1 = extremes[2];
            extreme4 = extremes[3];
            extreme2 = move(extreme1, azimuth, -1 * runwayDirection.getApproachSection().getThresholdLength());
            extreme3 = move(extreme4, azimuth, -1 * runwayDirection.getApproachSection().getThresholdLength());
        } else {
            extreme1 = extremes[0];
            extreme4 = extremes[1];
            extreme2 = move(extreme1, azimuth, runwayDirection.getApproachSection().getThresholdLength());
            extreme3 = move(extreme4, azimuth, runwayDirection.getApproachSection().getThresholdLength());
        }

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
