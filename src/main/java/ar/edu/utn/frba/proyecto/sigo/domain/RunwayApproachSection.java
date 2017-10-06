package ar.edu.utn.frba.proyecto.sigo.domain;

import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.google.common.base.MoreObjects;
import com.vividsolutions.jts.geom.Polygon;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "public.tbl_runway_approach_sections")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class RunwayApproachSection extends SigoDomain {
    @Id
    @SequenceGenerator(name = "runwayApproachSectionGenerator", sequenceName = "Runway_Approach_Section_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayApproachSectionGenerator")
    @Column(name = "section_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "direction_id")
    private RunwayDirection runwayDirection;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "threshold_length")
    private Double thresholdLength;

    @Column(name = "threshold_elevation")
    private Double thresholdElevation;

}
