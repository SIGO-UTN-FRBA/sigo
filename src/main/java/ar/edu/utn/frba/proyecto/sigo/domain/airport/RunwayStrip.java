package ar.edu.utn.frba.proyecto.sigo.domain.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_runway_strips")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class RunwayStrip extends SigoDomain {

    @Id
    @SequenceGenerator(
            name = "runwayStripGenerator",
            sequenceName = "RUNWAY_STRIP_SEQUENCE",
            allocationSize = 1,
            initialValue = 1000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "runwayStripGenerator"
    )
    @Column(name = "strip_id")
    private Long id;

    @Column(name = "strip_width")
    private Double width;

    @Column(name = "strip_length")
    private Double length;
}
