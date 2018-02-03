package ar.edu.utn.frba.proyecto.sigo.domain.location;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import com.google.common.base.MoreObjects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "public.tbl_political_location_types")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class PoliticalLocationType extends SigoDomain<Long> {
    @Id
    @SequenceGenerator(
            name = "PoliticalLocationTypeGenerator",
            sequenceName = "POLITICA_LOCATION_TYPE_SEQUENCE",
            allocationSize = 1,
            initialValue = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "PoliticalLocationTypeGenerator"
    )
    @Column(name = "type_id")
    private Long id;

    @Column
    private String name;

    @Column
    private Long index;


    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name:", name)
                .add("index", index)
                .toString();
    }
}