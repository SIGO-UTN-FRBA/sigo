package ar.edu.utn.frba.proyecto.sigo.domain.regulation.easa;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;

public class RegulationEASA extends Regulation {

    public RegulationEASA() {
        super((long)Regulations.EASA.ordinal(),
                "European Aviation Safety Agency (EASA)",
                "EASA",
                "Annex to ED Decision 2015/001/R. Issue 2, 29. January 2015"
        );
    }
}
