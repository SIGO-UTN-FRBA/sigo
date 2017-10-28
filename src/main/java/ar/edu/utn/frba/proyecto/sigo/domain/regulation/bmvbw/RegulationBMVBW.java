package ar.edu.utn.frba.proyecto.sigo.domain.regulation.bmvbw;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;

public class RegulationBMVBW extends Regulation {

    public RegulationBMVBW() {

        super((long)Regulations.BMVBW.ordinal(),
                "Bundesministerium für Verkehr, Bau- und Wohnungswesen (BMVBW)",
                "BMVBW (Germany)",
                "Richtlinien über die Hindernisfreiheit für Start- und Landebahnen mit Instrumentenflugbetrieb. 02.11.2001 (NfL I 328/01)."
        );
    }
}
