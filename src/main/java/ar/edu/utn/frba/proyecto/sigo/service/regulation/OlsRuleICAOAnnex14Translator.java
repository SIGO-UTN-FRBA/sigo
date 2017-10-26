package ar.edu.utn.frba.proyecto.sigo.service.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRulesICAOAnnex14Spec;
import ar.edu.utn.frba.proyecto.sigo.dto.regulation.OlsRuleICAOAnnex14DTO;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;

public class OlsRuleICAOAnnex14Translator extends Translator<OlsRulesICAOAnnex14Spec, OlsRuleICAOAnnex14DTO>{

    @Override
    public OlsRuleICAOAnnex14DTO getAsDTO(OlsRulesICAOAnnex14Spec domain) {
        return OlsRuleICAOAnnex14DTO.builder()
                .id(domain.getId())
                .ruleId(domain.getRule().getId())
                .runwayClassification(domain.getRunwayClassification().ordinal())
                .runwayCategory(domain.getRunwayCategory().ordinal())
                .runwayCodeNumber(domain.getRunwayCodeNumber().ordinal())
                .surface(domain.getSurface().ordinal())
                .property(domain.getProperty())
                .value(domain.getValue())
                .build();
    }

    @Override
    public OlsRulesICAOAnnex14Spec getAsDomain(OlsRuleICAOAnnex14DTO olsRuleICAOAnnex14DTO) {
        return null;
    }
}
