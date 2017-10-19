package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "public.tbl_analysis_objects")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data

public class AnalysisObject {
    @Id
    @SequenceGenerator(name = "analysisObjectsGenerator", sequenceName = "ANALYSIS_OBJECTS_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisObjectsGenerator")
    @Column(name = "analysis_object_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "object_id", foreignKey = @ForeignKey(name = "OBJECT_ANALYSIS_FK"))
    private PlacedObject placedObject;

    @ManyToOne
    private AnalysisCase analysisCase;
}