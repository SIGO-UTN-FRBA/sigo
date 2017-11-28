package ar.edu.utn.frba.proyecto.sigo.dto.regulation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OlsRuleFAADTO extends OlsRuleDTO {

    @Builder
    public OlsRuleFAADTO(Long id, Long ruleId, Integer regulationId) {
        super(id, ruleId, regulationId);
    }
}
