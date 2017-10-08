package ar.edu.utn.frba.proyecto.sigo.domain.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "public.tbl_runway_approach_sections")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class RunwayApproachSection extends SigoDomain {
    @Id
    @SequenceGenerator(name = "runwayApproachSectionGenerator", sequenceName = "Runway_Approach_Section_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayApproachSectionGenerator")
    @Column(name = "section_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id")
    private RunwayDirection runwayDirection;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "threshold_length")
    private Double thresholdLength;

    @Column(name = "threshold_elevation")
    private Double thresholdElevation;

}
