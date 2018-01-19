package ar.edu.utn.frba.proyecto.sigo.domain.object;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.Spatial;
import com.vividsolutions.jts.geom.Geometry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class ElevatedObject<T extends Geometry>
    extends SigoDomain
    implements Spatial<T>
{

    @Id
    @SequenceGenerator(name = "elevatedObjectGenerator", sequenceName = "ELEVATED_OBJECT_SEQUENCE", allocationSize = 1, initialValue = 120000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elevatedObjectGenerator")
    @Column(name = "object_id")
    protected Long id;

    @Column(name = "name")
    protected String name;

    @Column(name = "height_agl")
    protected Double heightAgl;

    @Column(name = "height_amls")
    protected Double heightAmls;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    protected ElevatedObjectTypes type;
}
