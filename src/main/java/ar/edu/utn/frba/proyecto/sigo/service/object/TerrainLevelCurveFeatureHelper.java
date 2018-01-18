package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.TerrainLevelCurve;
import com.vividsolutions.jts.geom.MultiLineString;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Singleton;

@Singleton
public class TerrainLevelCurveFeatureHelper {

    public SimpleFeature getFeature(TerrainLevelCurve terrainLevelCurve) {

        return SimpleFeatureBuilder.build(
                getFeatureSchema(),
                new Object[]{
                        terrainLevelCurve.getGeom(),
                        "Terrain Level Curve",
                        terrainLevelCurve.getName(),
                        terrainLevelCurve.getHeightAmls(),
                        terrainLevelCurve.getRepresentation(),
                        terrainLevelCurve.getSource()
                },
                terrainLevelCurve.getId().toString()
        );
    }

    private SimpleFeatureType getFeatureSchema() {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Terrain Level Curve");

        tb.add("geom", MultiLineString.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("name", String.class);
        tb.add("height_amls", Double.class);
        tb.add("representation", String.class);
        tb.add("source", String.class);

        return tb.buildFeatureType();
    }
}
