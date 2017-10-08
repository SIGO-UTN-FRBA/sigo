package ar.edu.utn.frba.proyecto.sigo.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "public.tbl_runway_takeoff_sections")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class RunwayTakeoffSection extends SigoDomain {
    @Id
    @SequenceGenerator(name = "runwayTakeoffSectionGenerator", sequenceName = "Runway_Takeoff_Section_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayTakeoffSectionGenerator")
    @Column(name = "section_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id")
    private RunwayDirection runwayDirection;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "clearway_length")
    private Double clearwayLength;

    @Column(name = "clearway_width")
    private Double clearwayWidth;

    @Column(name = "stopway_length")
    private Double stopwayLength;

}