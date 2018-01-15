package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.object.ElevatedObject;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true, exclude = "analysisCase")
@Entity
@Table(name = "public.tbl_analysis_objects")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AnalysisObject extends SigoDomain {
    @Id
    @SequenceGenerator(name = "analysisObjectsGenerator", sequenceName = "ANALYSIS_OBJECTS_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysisObjectsGenerator")
    @Column(name = "analysis_object_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "object_id", foreignKey = @ForeignKey(name = "OBJECT_ANALYSIS_FK"))
    private ElevatedObject elevatedObject;

    @ManyToOne
    private AnalysisCase analysisCase;

    @Column
    private Boolean included;
}