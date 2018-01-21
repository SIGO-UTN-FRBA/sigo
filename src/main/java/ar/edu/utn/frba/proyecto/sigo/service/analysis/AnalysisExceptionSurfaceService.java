package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionSurface;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class AnalysisExceptionSurfaceService extends SigoService<AnalysisExceptionSurface, AnalysisCase>{

    @Inject
    public AnalysisExceptionSurfaceService(HibernateUtil util) {
        super(AnalysisExceptionSurface.class, util.getSessionFactory());
    }

    public SimpleFeature getFeature(AnalysisExceptionSurface exception){

        return SimpleFeatureBuilder.build(
                getFeatureSchema(),
                new Object[]{
                        exception.getGeom(),
                        "Exception",
                        exception.getName(),
                        exception.getHeightAmls()
                },
                exception.getId().toString()
        );
    }

    private SimpleFeatureType getFeatureSchema() {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Exception");
        tb.add("geom", Polygon.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("name", String.class);
        tb.add("height_amls", Double.class);

        return tb.buildFeatureType();
    }
}
