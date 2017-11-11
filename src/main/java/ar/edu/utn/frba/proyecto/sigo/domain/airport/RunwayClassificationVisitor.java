package ar.edu.utn.frba.proyecto.sigo.domain.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.faa.RunwayClassificationFAA;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.icao.RunwayClassificationICAOAnnex14;

public interface RunwayClassificationVisitor<T> {

    T visitRunwayClassificationICAOAnnex14(RunwayClassificationICAOAnnex14 classification);

    T visitRunwayClassificationFAA(RunwayClassificationFAA classification);
}
