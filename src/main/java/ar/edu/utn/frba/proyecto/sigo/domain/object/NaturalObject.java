package ar.edu.utn.frba.proyecto.sigo.domain.object;

import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public abstract class NaturalObject<T extends Geometry> extends ElevatedObject<T> {

    public NaturalObject(Long id, String name, Double heightAgl, Double heightAmls, ElevatedObjectTypes type) {
        super(id, name, heightAgl, heightAmls, type);
    }
}
