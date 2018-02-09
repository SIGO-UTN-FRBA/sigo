@AnyMetaDef(
        name= "RestrictionMetaDef",
        metaType = "string",
        idType = "long",
        metaValues = {
                @MetaValue(value = "AnalysisExceptionSurface", targetEntity = AnalysisExceptionSurface.class),
                @MetaValue(value = "AnalysisSurface", targetEntity = AnalysisSurface.class)
        }
)

@AnyMetaDef(
        name= "SurfaceMetaDef",
        metaType = "string",
        idType = "long",
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
                @MetaValue(value = "ICAOAnnex14SurfaceTransitional", targetEntity = ICAOAnnex14SurfaceTransitional.class),
                @MetaValue(value = "ICAOAnnex14SurfaceOuterHorizontal", targetEntity = ICAOAnnex14SurfaceOuterHorizontal.class)
        }
)

package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.ols.icao.*;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;