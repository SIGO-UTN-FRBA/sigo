package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "public.tbl_analysis_case_status_registry")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class AnalysisCaseStatusRegistry {
    @Id
    @SequenceGenerator(name = "analysisCaseStatusRegistryGenerator", sequenceName = "ANALYSIS_CASE_STATUS_REGISTRY_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisCaseStatusRegistryGenerator")
    @Column(name = "register_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase analysisCase;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "status_id")
    private AnalysisCaseStatuses status;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

}