package ar.edu.utn.frba.proyecto.sigo.domain.airport.icao;

import javax.persistence.*;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayClassification;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeLetters;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayClassificationVisitor;
import lombok.*;

@Entity
@Table(name = "public.tbl_runway_classification_ICAOAnnex14")
@PrimaryKeyJoinColumn(name = "classification_id")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class RunwayClassificationICAOAnnex14 extends RunwayClassification {

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "classification")
    private ICAOAnnex14RunwayClassifications runwayClassification;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category")
    private ICAOAnnex14RunwayCategories runwayCategory;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type_number")
    private ICAOAnnex14RunwayCodeNumbers runwayTypeNumber;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type_letter")
    private ICAOAnnex14RunwayCodeLetters runwayTypeLetter;

    @Builder
    public RunwayClassificationICAOAnnex14(
            Long id,
            RunwayDirection runwayDirection,
            ICAOAnnex14RunwayClassifications runwayClassification,
            ICAOAnnex14RunwayCategories runwayCategory,
            ICAOAnnex14RunwayCodeNumbers runwayTypeNumber,
            ICAOAnnex14RunwayCodeLetters runwayTypeLetter
    ){
        super(id, runwayDirection);
        this.runwayClassification = runwayClassification;
        this.runwayCategory = runwayCategory;
        this.runwayTypeLetter = runwayTypeLetter;
        this.runwayTypeNumber = runwayTypeNumber;
    }

    @Override
    public <T> T accept(RunwayClassificationVisitor<T> visitor) {
        return visitor.visitRunwayClassificationICAOAnnex14(this);
    }
}