package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "public.tbl_analysis_case_status")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class AnalysisCaseStatus {
    @Id
    @SequenceGenerator(name = "analysCaseStatusGenerator", sequenceName = "AnalysCaseStatus_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysCaseStatusGenerator")
    @Column(name = "status_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

}