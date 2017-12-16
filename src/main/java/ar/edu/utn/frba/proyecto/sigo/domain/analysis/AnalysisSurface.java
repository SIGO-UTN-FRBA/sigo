package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14Surfaces;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Geometry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "public.tbl_analysis_surfaces")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisSurface extends SigoDomain {

    @Id
    @SequenceGenerator(name = "analysisSurfaceGenerator", sequenceName = "ANALYSIS_SURFACE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisSurfaceGenerator")
    @Column(name = "analysis_surface_id")
    private Long id;


    @AnyMetaDef( name= "SurfaceMetaDef", metaType = "string", idType = "long",
            metaValues = {
                    @MetaValue(value = "ICAOAnnex14SurfaceStrip", targetEntity = ICAOAnnex14SurfaceStrip.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceInnerHorizontal", targetEntity = ICAOAnnex14SurfaceInnerHorizontal.class)
            }
    )
    
    @Any(
            metaDef = "SurfaceMetaDef",
            metaColumn = @Column( name = "surface_type" )
    )
    @JoinColumn( name = "surface_id" )
    private ObstacleLimitationSurface surface;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysisCase;

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private RunwayDirection direction;

    @OneToMany(mappedBy = "surface", cascade = CascadeType.REMOVE)
    private List<AnalysisObstacle> obstacles = Lists.newArrayList();

    @Column(name="geom")
    private Geometry geometry;


}
