package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "public.tbl_analysis_area_surfaces")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisAreaSurface {
    @Id
    @SequenceGenerator(name = "analysisAreaSurface", sequenceName = "ANALYSIS_AREA_SURFACE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisAreaSurface")
    @Column(name = "surface_id")
    private Long id;

    @ManyToOne
    private AnalysisArea area;

    @Column(name = "name")
    private String name;

    @Column(name = "properties")
    private String properties;

    @Column(name = "geom")
    private MultiPolygon geom;

}