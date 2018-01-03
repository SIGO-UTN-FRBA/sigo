package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproach;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachFirstSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachHorizontalSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceApproachSecondSection;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceBalkedLanding;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerApproach;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceInnerTransitional;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceTakeoffClimb;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.ICAOAnnex14SurfaceTransitional;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MetaValue;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = "analysisCase")
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
                    @MetaValue(value = "ICAOAnnex14SurfaceInnerHorizontal", targetEntity = ICAOAnnex14SurfaceInnerHorizontal.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceConical", targetEntity = ICAOAnnex14SurfaceConical.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceApproach", targetEntity = ICAOAnnex14SurfaceApproach.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceApproachFirstSection", targetEntity = ICAOAnnex14SurfaceApproachFirstSection.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceApproachHorizontalSection", targetEntity = ICAOAnnex14SurfaceApproachHorizontalSection.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceApproachSecondSection", targetEntity = ICAOAnnex14SurfaceApproachSecondSection.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceBalkedLanding", targetEntity = ICAOAnnex14SurfaceBalkedLanding.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceInnerApproach", targetEntity = ICAOAnnex14SurfaceInnerApproach.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceInnerTransitional", targetEntity = ICAOAnnex14SurfaceInnerTransitional.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceTakeoffClimb", targetEntity = ICAOAnnex14SurfaceTakeoffClimb.class),
                    @MetaValue(value = "ICAOAnnex14SurfaceTransitional", targetEntity = ICAOAnnex14SurfaceTransitional.class)
            }
    )
    @Any(
            metaDef = "SurfaceMetaDef",
            metaColumn = @Column( name = "surface_type" ),
            fetch = FetchType.EAGER
    )
    @Cascade( { org.hibernate.annotations.CascadeType.ALL })
    @JoinColumn( name = "surface_id")
    private ObstacleLimitationSurface surface;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysisCase;

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private RunwayDirection direction;

    @OneToMany(mappedBy = "surface", cascade = CascadeType.ALL)
    private List<AnalysisObstacle> obstacles = Lists.newArrayList();
}
