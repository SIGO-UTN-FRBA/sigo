package ar.edu.utn.frba.proyecto.sigo.domain;

import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "public.tbl_analysis_area_surfaces")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class AnalysisAreaSurface {
    @Id
    @SequenceGenerator(name = "analysAreaSurface", sequenceName = "ANALYS_AREA_SURFACE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysAreaSurface")
    @Column(name = "surface_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private AnalysisArea analysisArea;

    @Column(name = "name")
    private String name;

    @Column(name = "properties")
    private String properties;

    @Column(name = "geom")
    private MultiPolygon geom;

}