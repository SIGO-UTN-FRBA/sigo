package ar.edu.utn.frba.proyecto.sigo.domain.object;

import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class NaturalObject<T extends Geometry> extends ElevatedObject<T> {

    public NaturalObject(Long id, String name, Double heightAgl, Double heightAmls, ElevatedObjectTypes type, T geom) {
        super(id, name, heightAgl, heightAmls, type, geom);
    }
}
