package ar.edu.utn.frba.proyecto.sigo.domain.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import com.google.common.base.MoreObjects;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true, exclude = "runwayDirection")
@Entity
@Table(name = "public.tbl_runway_takeoff_sections")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class RunwayTakeoffSection extends SigoDomain {
    @Id
    @SequenceGenerator(
            name = "runwayTakeoffSectionGenerator",
            sequenceName = "Runway_Takeoff_Section_SEQUENCE",
            allocationSize = 1,
            initialValue = 1000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "runwayTakeoffSectionGenerator"
    )
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


    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("direction_id", runwayDirection.getId())
                .add("enabled", enabled)
                .add("stopway_length", stopwayLength)
                .add("clearwayWidth", clearwayWidth)
                .add("clearway_length", clearwayLength)
                .toString();
    }

}