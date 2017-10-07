package ar.edu.utn.frba.proyecto.sigo.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "public.tbl_analysis_objects")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class AnalysisObjects {
    @Id
    @SequenceGenerator(name = "analysisObjectsGenerator", sequenceName = "ANALYSIS_OBJECTS_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisObjectsGenerator")
    @Column(name = "case_id")
    private Long id;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "action")
    private Enum action;

}