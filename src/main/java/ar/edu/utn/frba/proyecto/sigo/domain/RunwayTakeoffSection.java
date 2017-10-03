package ar.edu.utn.frba.proyecto.sigo.domain;


import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.google.common.base.MoreObjects;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "public.tbl_runway_takeoff_sections")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class RunwayTakeoffSection {
    @Id
    @SequenceGenerator(name = "runwayTakeoffSectionGenerator", sequenceName = "Runway_Takeoff_Section_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "runwayTakeoffSectionGenerator")
    @Column(name = "section_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "direction_id")
    private RunwayDirection runwaydirection;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "clearway_length")
    private Long clearwayLength;

    @Column(name = "clearway_width")
    private Long clearwayWidth;

    @Column(name = "stopway_length")
    private Long stopwayLength;

}