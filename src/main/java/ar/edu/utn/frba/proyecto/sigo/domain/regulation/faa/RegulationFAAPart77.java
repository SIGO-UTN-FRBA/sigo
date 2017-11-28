package ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;

public class RegulationFAAPart77 extends Regulation {

    public RegulationFAAPart77() {

        super((long) Regulations.FAA_CFR_PART_77.ordinal(),
                "Federal Aviation Administration (FAA)",
                "FAA - CFR Part 77",
                "Code of Federal Regulations Part 77. Date: August 05, 2016"
        );
    }
}
