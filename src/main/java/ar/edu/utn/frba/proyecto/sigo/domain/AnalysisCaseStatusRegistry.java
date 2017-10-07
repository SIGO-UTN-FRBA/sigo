package ar.edu.utn.frba.proyecto.sigo.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "public.tbl_analysis_case_status_registry")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class AnalysisCaseStatusRegistry {
    @Id
    @SequenceGenerator(name = "analysCaseStatusRegistryGenerator", sequenceName = "ANALYSIS_CASE_STATUS_REGISTRY_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysCaseStatusRegistryGenerator")
    @Column(name = "register_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysCase;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private AnalysisCaseStatus analysCaseStatus;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

}