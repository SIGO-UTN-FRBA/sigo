package ar.edu.utn.frba.proyecto.sigo.domain;

import com.vividsolutions.jts.geom.MultiPolygon;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "public.tbl_analysis_cases")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class AnalysisCase {
    @Id
    @SequenceGenerator(name = "analysisCaseGenerator", sequenceName = "ANALYSIS_CASE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisCaseGenerator")
    @Column(name = "case_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private AnalysisCaseStatus analysisCaseStatus;


    @ManyToOne
    @JoinColumn(name = "aerodrome_id")
    private Airport aerodrome;

    @ManyToOne
    @JoinColumn(name = "regulation_id")
    private Regulation regulation;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private AnalysisArea analysisarea;

   /*
    TODO
    @OneToMany(mappedBy="analyscase")
    private Set<AnalysCaseStatusRegistry> analyscasestatusregistrys;

    @OneToMany(mappedBy="analyscase")
    private Set<AnalysException> analysexceptions;*/
}
