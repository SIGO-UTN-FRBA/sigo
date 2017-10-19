package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "public.tbl_analysis_cases")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
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

    @OneToOne(
            mappedBy = "analysisCase",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @LazyToOne( LazyToOneOption.NO_PROXY )
    private AnalysisArea area;

    @OneToMany(mappedBy = "analysisCase", cascade = CascadeType.REMOVE)
    private List<AnalysisObject> objects;

   /*
    TODO
    @OneToMany(mappedBy="analyscase")
    private Set<AnalysCaseStatusRegistry> analyscasestatusregistrys;

    @OneToMany(mappedBy="analyscase")
    private Set<AnalysException> analysexceptions;*/
}
