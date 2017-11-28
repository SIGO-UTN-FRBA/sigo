package ar.edu.utn.frba.proyecto.sigo.service.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceApproach;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceApproachFirstSection;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceApproachHorizontalSection;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceApproachSecondSection;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceBalkedLanding;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceInnerApproach;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceInnerHorizontal;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceInnerTransitional;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceStrip;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceTakeoffClimb;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceTransitional;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;

import java.util.List;

public class ICAOAnnex14SurfacesFactory {

    static ICAOAnnex14SurfaceConical createConicalSurface(List<OlsRuleICAOAnnex14> rules){

        ICAOAnnex14SurfaceConical.ICAOAnnex14SurfaceConicalBuilder builder = ICAOAnnex14SurfaceConical.builder();

        rules.forEach(icaoRule -> {

            switch (icaoRule.getProperty()){
                case "Slope":
                    builder.slope(icaoRule.getValue());
                    break;
                case "Height":
                    builder.height(icaoRule.getValue());
                    break;
                case "Radius":
                    builder.ratio(icaoRule.getValue());
            }
        });

        return builder.build();
    }

    static ICAOAnnex14SurfaceApproach createApproachSurface(List<OlsRuleICAOAnnex14> rules) {
        ICAOAnnex14SurfaceApproach.ICAOAnnex14SurfaceApproachBuilder builder = ICAOAnnex14SurfaceApproach.builder();

        rules.forEach( icaoRule ->{

            switch (icaoRule.getProperty()){
                case "Length of inner edge":
                    builder.lengthOfInnerEdge(icaoRule.getValue());
                    break;
                case "Distance from threshold":
                    builder.distanceFromThreshold(icaoRule.getValue());
                    break;
                case "Divergence (each side)":
                    builder.divergence(icaoRule.getValue());
            }

        });

        return builder.build();
    }

    static ICAOAnnex14SurfaceApproachFirstSection createApproachFirstSectionSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceApproachFirstSection.ICAOAnnex14SurfaceApproachFirstSectionBuilder builder = ICAOAnnex14SurfaceApproachFirstSection.builder();

        rules.forEach( icaoRule ->{

            switch (icaoRule.getProperty()){
                case "Length":
                    builder.length(icaoRule.getValue());
                    break;
                case "Slope":
                    builder.slope(icaoRule.getValue());
                    break;
            }
        });

        return builder.build();
    }

    static ICAOAnnex14SurfaceApproachSecondSection createApproachSecondSectionSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceApproachSecondSection.ICAOAnnex14SurfaceApproachSecondSectionBuilder builder = ICAOAnnex14SurfaceApproachSecondSection.builder();

        rules.forEach( icaoRule ->{
            switch (icaoRule.getProperty()){
                case "Length":
                    builder.length(icaoRule.getValue());
                    break;
                case "Slope":
                    builder.slope(icaoRule.getValue());
                    break;
            }
        });

        return builder.build();
    }

    static ICAOAnnex14SurfaceApproachHorizontalSection createApproachHorizontalSectionSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceApproachHorizontalSection.ICAOAnnex14SurfaceApproachHorizontalSectionBuilder builder = ICAOAnnex14SurfaceApproachHorizontalSection.builder();

        rules.forEach( icaoRule ->{
            switch (icaoRule.getProperty()){
                case "Length":
                    builder.length(icaoRule.getValue());
                    break;
                case "Slope":
                    builder.totalLength(icaoRule.getValue());
                    break;
            }
        });

        return builder.build();
    }

    static ICAOAnnex14SurfaceTransitional createTransitionalSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceTransitional.ICAOAnnex14SurfaceTransitionalBuilder builder = ICAOAnnex14SurfaceTransitional.builder();

        builder.slope(rules.get(0).getValue());

        return builder.build();
    }

    static ICAOAnnex14SurfaceInnerTransitional createInnerTransitionalSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceInnerTransitional.ICAOAnnex14SurfaceInnerTransitionalBuilder builder = ICAOAnnex14SurfaceInnerTransitional.builder();

        builder.slope(rules.get(0).getValue());

        return builder.build();
    }

    static ICAOAnnex14SurfaceBalkedLanding createBalkedLandingSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceBalkedLanding.ICAOAnnex14SurfaceBalkedLandingBuilder builder = ICAOAnnex14SurfaceBalkedLanding.builder();

        rules.forEach( icaoRule ->{

            switch (icaoRule.getProperty()){
                case "Distance from threshold":
                    builder.distanceFromThreshold(icaoRule.getValue());
                    break;
                case "Length of inner edge":
                    builder.lengthOfInnerEdge(icaoRule.getValue());
                    break;
                case "Slope":
                    builder.slope(icaoRule.getValue());
                    break;
                case "Divergence (each side)":
                    builder.divergence(icaoRule.getValue());
            }
        });

        return builder.build();
    }

    static ICAOAnnex14SurfaceStrip createStripSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceStrip.ICAOAnnex14SurfaceStripBuilder builder = ICAOAnnex14SurfaceStrip.builder();

        rules.forEach( icaoRule ->{

            switch (icaoRule.getProperty()){
                case "Width":
                    builder.width(icaoRule.getValue());
                    break;
                case "Length":
                    builder.length(icaoRule.getValue());
                    break;
            }
        });

        return builder.build();
    }

    static ICAOAnnex14SurfaceTakeoffClimb createTakeoffClimbSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceTakeoffClimb.ICAOAnnex14SurfaceTakeoffClimbBuilder builder = ICAOAnnex14SurfaceTakeoffClimb.builder();

        rules.forEach( icaoRule ->{

            switch (icaoRule.getProperty()){
                case "Slope":
                    builder.slope(icaoRule.getValue());
                    break;
                case "Length":
                    builder.length(icaoRule.getValue());
                    break;
                case "Length of inner edge":
                    builder.lengthOfInnerEdge(icaoRule.getValue());
                    break;
                case "Divergence (each side)":
                    builder.divergence(icaoRule.getValue());
                    break;
                case "Distance from runway end":
                    builder.distanceFromRunwayEnds(icaoRule.getValue());
                    break;
                case "Final width":
                    builder.finalWidth(icaoRule.getValue());
                    break;
            }
        });

        return builder.build();
    }

    static ICAOAnnex14SurfaceInnerHorizontal createInnerHorizontalSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceInnerHorizontal.ICAOAnnex14SurfaceInnerHorizontalBuilder builder = ICAOAnnex14SurfaceInnerHorizontal.builder();

        rules.forEach( icaoRule -> {

            switch (icaoRule.getProperty()){
                case "Height":
                    builder.height(icaoRule.getValue());
                    break;
                case "Radius":
                    builder.radius(icaoRule.getValue());
                    break;
            }
        });

        return builder.build();
    }

    static ICAOAnnex14SurfaceInnerApproach createInnerApproachSurface(List<OlsRuleICAOAnnex14> rules){
        ICAOAnnex14SurfaceInnerApproach.ICAOAnnex14SurfaceInnerApproachBuilder builder = ICAOAnnex14SurfaceInnerApproach.builder();

        rules.forEach( icaoRule ->{

            switch (icaoRule.getProperty()){
                case "Distance from threshold":
                    builder.distanceFromThreshold(icaoRule.getValue());
                    break;
                case "Width":
                    builder.width(icaoRule.getValue());
                    break;
                case "Slope":
                    builder.slope(icaoRule.getValue());
                    break;
                case "Length":
                    builder.length(icaoRule.getValue());
                    break;
            }
        });

        return builder.build();
    }

}
