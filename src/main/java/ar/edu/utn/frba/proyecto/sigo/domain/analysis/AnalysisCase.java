package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "public.tbl_analysis_cases")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisCase extends SigoDomain {
    @Id
    @SequenceGenerator(name = "analysisCaseGenerator", sequenceName = "ANALYSIS_CASE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisCaseGenerator")
    @Column(name = "case_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "case_id")
    private AnalysisCase baseCase;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_id")
    private AnalysisCaseStatuses status;

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
    private Set<AnalysisCaseStatusRegistry> analyscasestatusregistrys;
    */

    @OneToMany(mappedBy="analysisCase", cascade = CascadeType.REMOVE)
    private Set<AnalysisException> exceptions;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "stage_id")
    private AnalysisWizardStages stage;

    @Column(name="creation_date")
    private LocalDateTime creationDate;

   public Regulations getRegulation(){
       return this.getAerodrome().getRegulation();
   }
}
