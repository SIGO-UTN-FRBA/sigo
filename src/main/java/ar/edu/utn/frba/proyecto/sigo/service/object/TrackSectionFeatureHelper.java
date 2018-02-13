package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.TrackSection;
import com.vividsolutions.jts.geom.LineString;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Singleton;

@Singleton
public class TrackSectionFeatureHelper {

    public SimpleFeature getFeature(TrackSection trackSection) {

        return SimpleFeatureBuilder.build(
                getFeatureSchema(),
                new Object[]{
                        trackSection.getGeom(),
                        trackSection.getSubtype().description(),
                        trackSection.getName(),
                        trackSection.getHeightAmls(),
                        trackSection.getHeightAgl()
                },
                trackSection.getId().toString()
        );
    }

    private SimpleFeatureType getFeatureSchema() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Object");
        tb.add("geom", LineString.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("name", String.class);
        tb.add("height_amls", Double.class);
        tb.add("height_agl", Double.class);

        return tb.buildFeatureType();
    }
}
