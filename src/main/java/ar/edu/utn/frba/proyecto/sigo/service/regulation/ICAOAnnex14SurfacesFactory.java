package ar.edu.utn.frba.proyecto.sigo.service.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14SurfaceConical;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRulesICAOAnnex14Spec;

import java.util.List;

public class ICAOAnnex14SurfacesFactory {

    public static ICAOAnnex14SurfaceConical createConicalSurface(List<OlsRule> rules){

        ICAOAnnex14SurfaceConical.ICAOAnnex14SurfaceConicalBuilder builder = ICAOAnnex14SurfaceConical.builder();

        rules.forEach(r -> {

            OlsRulesICAOAnnex14Spec icaoRule = r.getIcaoRule();

            switch (icaoRule.getProperty()){
                case "Slope":
                    builder.slope(Double.valueOf(icaoRule.getValue()));
                    break;
                case "Height":
                    builder.height(Double.valueOf(icaoRule.getValue()));
                    break;
                case "Radius":
                    builder.ratio(Double.valueOf(icaoRule.getValue()));
            }
        });

        return builder.build();
    }
}
