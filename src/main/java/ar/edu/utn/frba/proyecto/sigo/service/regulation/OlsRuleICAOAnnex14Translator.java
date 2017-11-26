package ar.edu.utn.frba.proyecto.sigo.service.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRulesICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.dto.regulation.OlsRuleICAOAnnex14DTO;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;

public class OlsRuleICAOAnnex14Translator extends Translator<OlsRulesICAOAnnex14, OlsRuleICAOAnnex14DTO>{

    @Override
    public OlsRuleICAOAnnex14DTO getAsDTO(OlsRulesICAOAnnex14 domain) {
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
    public OlsRulesICAOAnnex14 getAsDomain(OlsRuleICAOAnnex14DTO olsRuleICAOAnnex14DTO) {
        return null;
    }
}
