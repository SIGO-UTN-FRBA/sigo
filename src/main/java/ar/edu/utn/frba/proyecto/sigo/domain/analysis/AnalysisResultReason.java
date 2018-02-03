package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "public.tbl_analysis_result_reasons")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnalysisResultReason extends SigoDomain<Long> {

    @Id
    @SequenceGenerator(
            name = "analysisResultReasonGenerator",
            sequenceName = "ANALYSIS_RESULT_REASON_SEQUENCE",
            allocationSize = 1,
            initialValue = 10
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisResultReasonGenerator")
    @Column(name = "reason_id")
    private Long id;

    @Column
    private String description;

    @Column
    private Boolean obstacle;

    @Column
    private Boolean keep;

}
