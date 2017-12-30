package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
    private PlacedObject placedObject;

    @ManyToOne
    private AnalysisCase analysisCase;

    @Column
    private Boolean included;
}