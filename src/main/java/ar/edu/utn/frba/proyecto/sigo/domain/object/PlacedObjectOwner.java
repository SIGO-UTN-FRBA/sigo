package ar.edu.utn.frba.proyecto.sigo.domain.object;


import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import com.google.common.base.MoreObjects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "public.tbl_placed_object_owner")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class PlacedObjectOwner extends SigoDomain {
    @Id
    @SequenceGenerator(name = "placedObjectOwnerGenerator", sequenceName = "PLACED_OBJECT_OWNER_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "placedObjectOwnerGenerator")
    @Column(name = "owner_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone1")
    private String phone1;

    @Column(name = "phone2")
    private String phone2;


    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name:", name)
                .toString();
    }
}