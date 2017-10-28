package ar.edu.utn.frba.proyecto.sigo.domain.object;


import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;
import lombok.*;
import com.vividsolutions.jts.geom.MultiPolygon;

@Entity
@Table(name = "public.tbl_placed_object_building_spec")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder

public class PlacedObjectBuildingSpec implements PlacedObjectSpec<MultiPolygon>{
    @Id
    @SequenceGenerator(name = "placedObjectBuildingSpecGenerator", sequenceName = "PLACED_OBJECT_BUILDING_SPEC_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "placedObjectBuildingSpecGenerator")
    @Column(name = "spec_id")
    private Long id;

    @Column(name = "geom")
    private MultiPolygon geom;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id")
    private PlacedObject placedObject;

    @Override
    public Class getGeomClass() {
        return MultiPolygon.class;
    }
}