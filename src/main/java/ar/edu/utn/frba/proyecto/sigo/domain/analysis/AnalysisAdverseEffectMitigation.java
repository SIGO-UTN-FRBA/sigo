package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true, exclude = "aspect")
@Entity
@Table(name = "tbl_analysis_adverse_effect_mitigations")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalysisAdverseEffectMitigation extends SigoDomain<Long> {

    @Id
    @Column(name = "mitigation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aspect_id")
    private AnalysisAdverseEffectAspect aspect;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "operational_damage")
    private Boolean operationDamage;
}
