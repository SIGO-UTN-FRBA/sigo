package ar.edu.utn.frba.proyecto.sigo.translator.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRuleVisitor;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.OlsRulesFAA;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.dto.regulation.OlsRuleDTO;
import ar.edu.utn.frba.proyecto.sigo.dto.regulation.OlsRuleICAOAnnex14DTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class OlsRuleTranslator
        extends SigoTranslator<OlsRule, OlsRuleDTO>
        implements OlsRuleVisitor<OlsRuleDTO>
{

    @Inject
    public OlsRuleTranslator(Gson objectMapper) {
        super(objectMapper, OlsRuleDTO.class, OlsRule.class);
    }

    @Override
    public OlsRuleDTO getAsDTO(OlsRule domain) {
        return domain.accept(this);
    }

    @Override
    public OlsRule getAsDomain(OlsRuleDTO olsRuleICAOAnnex14DTO) {
        return null;
    }

    @Override
    public OlsRuleDTO visitOlsRuleICAOAnnex14(OlsRuleICAOAnnex14 rule) {
        OlsRuleICAOAnnex14DTO.OlsRuleICAOAnnex14DTOBuilder builder = OlsRuleICAOAnnex14DTO.builder();

        Optional.ofNullable(rule.getRunwayCategory())
                .ifPresent(v -> builder.runwayCategory(v.ordinal()));

        builder
                .id(rule.getId())
                .regulationId(rule.getRegulation().ordinal())
                .surface(rule.getSurface().ordinal())
                .runwayClassification(rule.getRunwayClassification().ordinal())
                .runwayCodeNumber(rule.getRunwayCodeNumber().ordinal())
                .propertyName(rule.getPropertyName())
                .propertyCode(rule.getPropertyCode())
                .value(rule.getValue());

        return builder.build();
    }

    @Override
    public OlsRuleDTO visitOlsRuleFAA(OlsRulesFAA rule) {
        throw new NotImplementedException();
    }
}
