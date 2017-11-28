package ar.edu.utn.frba.proyecto.sigo.dto.regulation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class OlsRuleDTO {

    private Long id;
    private Long ruleId;
    private Integer regulationId;
}
