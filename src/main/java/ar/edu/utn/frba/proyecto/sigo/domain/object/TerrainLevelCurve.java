package ar.edu.utn.frba.proyecto.sigo.domain.object;

import com.vividsolutions.jts.geom.MultiLineString;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "public.tbl_level_curves")
@NoArgsConstructor
@Data
public class TerrainLevelCurve extends NaturalObject<MultiLineString> {
    /**
     * Tipo de representación hispográfica.
     * Clasificación de la representación de las curvas de nivel de acuerdo a su origen, intervalo de representación y características morfológicas del terreno.
     */
    @Column(name = "hqc")
    private String representation;

    /**
     * Autoridad de fuente.
     * Nombre de la autoridad responsable de la información utilizada.
     */
    @Column(name = "sag")
    private String source;

    @Column(name = "geom")
    private MultiLineString geom;

    @Override
    public ElevatedObjectTypes getType() {
        return ElevatedObjectTypes.LEVEL_CURVE;
    }

    @Builder
    public TerrainLevelCurve(Long id, String name, Double heightAgl, Double heightAmls, String representation, String source, MultiLineString geom) {
        super(id, name, heightAgl, heightAmls);
        this.representation = representation;
        this.source = source;
        this.geom = geom;
    }
}
