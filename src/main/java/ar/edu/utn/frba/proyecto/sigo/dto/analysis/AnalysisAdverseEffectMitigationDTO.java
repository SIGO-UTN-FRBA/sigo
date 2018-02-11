package ar.edu.utn.frba.proyecto.sigo.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AnalysisAdverseEffectMitigationDTO {

    private Long id;
    private String name;
    private String description;
    private Boolean operationDamage;
    private Long aspectId;
}
