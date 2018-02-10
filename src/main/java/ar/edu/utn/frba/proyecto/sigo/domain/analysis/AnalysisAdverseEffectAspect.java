package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import com.google.common.collect.Sets;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_analysis_adverse_effect_aspects")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalysisAdverseEffectAspect extends SigoDomain<Long> {

    @Id
    @Column(name = "aspect_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "aspect", cascade = CascadeType.ALL)
    private Set<AnalysisAdverseEffectMitigation> mitigations = Sets.newHashSet();
}
