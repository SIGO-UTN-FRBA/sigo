package ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;

public class RegulationFAA13A extends Regulation {

    public RegulationFAA13A() {

        super((long) Regulations.FAA_AC150_5300_13A.ordinal(),
                "Federal Aviation Administration (FAA)",
                "FAA - AC150-5300-13A",
                "AC Nro: 150/5300-13A (including change 1). Date February 26, 2014"
        );
    }
}
