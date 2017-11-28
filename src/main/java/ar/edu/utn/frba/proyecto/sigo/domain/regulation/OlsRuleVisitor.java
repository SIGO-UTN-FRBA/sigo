package ar.edu.utn.frba.proyecto.sigo.domain.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.OlsRulesFAA;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;

public interface OlsRuleVisitor<T> {

    T visitOlsRuleICAOAnnex14(OlsRuleICAOAnnex14 rule);
    T visitOlsRuleFAA(OlsRulesFAA rule);
}
