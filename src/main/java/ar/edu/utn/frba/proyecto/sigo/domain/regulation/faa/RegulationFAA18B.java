package ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;

public class RegulationFAA18B extends Regulation {

    public RegulationFAA18B() {

        super((long) Regulations.FAA_AC150_5300_18B.ordinal(),
                "Federal Aviation Administration (FAA)",
                "FAA - AC150-5300-18B",
                "AC Nro: 150/5300-18B. Date May 21, 2009"
        );
    }
}
