package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectBuilding;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividual;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectOverheadWire;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectVisitor;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Singleton;

@Singleton
public class PlacedObjectFeatureService implements PlacedObjectVisitor<SimpleFeature>{

    public SimpleFeature getFeature(PlacedObject placedObject) {

        return placedObject.accept(this);
    }

    @Override
    public SimpleFeature visitPlacedObjectBuilding(PlacedObjectBuilding building) {
        return SimpleFeatureBuilder.build(
                getFeatureSchema(MultiPolygon.class),
                new Object[]{
                        building.getGeom(),
                        "Building",
                        building.getName(),
                        building.getSubtype(),
                        building.getOwner().getName(),
                        building.getTemporary(),
                        building.getVerified(),
                        building.getLighting().type(),
                        building.getMarkIndicator().type(),
                        building.getHeightAgl(),
                        building.getHeightAmls()
                },
                building.getId().toString()
        );
    }


    @Override
    public SimpleFeature visitPlacedObjectIndividual(PlacedObjectIndividual individual) {
        return SimpleFeatureBuilder.build(
                getFeatureSchema(Point.class),
                new Object[]{
                        individual.getGeom(),
                        "Individual Object",
                        individual.getName(),
                        individual.getSubtype(),
                        individual.getOwner().getName(),
                        individual.getTemporary(),
                        individual.getVerified(),
                        individual.getLighting().type(),
                        individual.getMarkIndicator().type(),
                        individual.getHeightAgl(),
                        individual.getHeightAmls()
                },
                individual.getId().toString()
        );
    }

    @Override
    public SimpleFeature visitPlacedObjectOverheadWire(PlacedObjectOverheadWire wire) {
        return SimpleFeatureBuilder.build(
                getFeatureSchema(LineString.class),
                new Object[]{
                        wire.getGeom(),
                        "Overhead Wire",
                        wire.getName(),
                        wire.getSubtype(),
                        wire.getOwner().getName(),
                        wire.getTemporary(),
                        wire.getVerified(),
                        wire.getLighting().type(),
                        wire.getMarkIndicator().type(),
                        wire.getHeightAgl(),
                        wire.getHeightAmls()
                },
                wire.getId().toString()
        );
    }

    private <T extends Geometry>SimpleFeatureType getFeatureSchema(Class<T> clazz) {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Object");
        tb.add("geom", clazz, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("name", String.class);
        tb.add("type", String.class);
        tb.add("owner", String.class);
        tb.add("temporary", Boolean.class);
        tb.add("verified", Boolean.class);
        tb.add("lighting", String.class);
        tb.add("mark indicator", String.class);
        tb.add("height_agl", Double.class);
        tb.add("height_amls", Double.class);

        return tb.buildFeatureType();
    }

}
