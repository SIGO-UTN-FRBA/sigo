package ar.edu.utn.frba.proyecto.sigo.domain;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "public.tbl_analysis_areas")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class AnalysisArea {
    @Id
    @SequenceGenerator(name = "analysAreaGenerator", sequenceName = "ANALYS_AREA_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysAreaGenerator")
    @Column(name = "area_id")
    private Long id;

    @OneToOne(mappedBy="analysisarea")
    private AnalysisCase analysisCase;

}
