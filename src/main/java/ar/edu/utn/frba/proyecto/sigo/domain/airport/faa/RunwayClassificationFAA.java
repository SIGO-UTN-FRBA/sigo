package ar.edu.utn.frba.proyecto.sigo.domain.airport.faa;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayClassification;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAAAircraftApproachCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAAAircraftClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAARunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAARunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.FAARunwaysTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayClassificationVisitor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "public.tbl_runway_classification_faa")
@PrimaryKeyJoinColumn(name = "classification_id")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class RunwayClassificationFAA extends RunwayClassification {

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "runway_classification")
    private FAARunwayClassifications runwayClassification;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "runway_category")
    private FAARunwayCategories runwayCategory;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "aircraft_approach_category")
    private FAAAircraftApproachCategories aircraftApproachCategory;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "aircraft_classification")
    private FAAAircraftClassifications aircraftClassification;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "runway_type_letter")
    private FAARunwaysTypes runwayTypeLetter;

    @Builder
    public RunwayClassificationFAA(
            Long id,
            RunwayDirection runwayDirection,
            FAARunwayClassifications classification,
            FAARunwayCategories category,
            FAAAircraftApproachCategories aircraftApproachCategory,
            FAAAircraftClassifications aircraftClassification,
            FAARunwaysTypes letter
    ){
        super(id, runwayDirection);

        this.runwayClassification = classification;
        this.runwayCategory = category;
        this.aircraftApproachCategory = aircraftApproachCategory;
        this.aircraftClassification = aircraftClassification;
        this.runwayTypeLetter = letter;
    }

    @Override
    public <T> T accept(RunwayClassificationVisitor<T> visitor) {
        return visitor.visitRunwayClassificationFAA(this);
    }
}
