package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;


public class RegulationICAOAnnex14 extends Regulation {

    public RegulationICAOAnnex14() {
        super((long)Regulations.ICAO_ANNEX_14.ordinal(),
                "International Civil Aviation Organization (ICAO)",
                "ICAO Annex 14",
                "Annex 14, Volume I, Aerodrome Design and Operations. Fifth Edition, July 2009."
        );
    }
}
