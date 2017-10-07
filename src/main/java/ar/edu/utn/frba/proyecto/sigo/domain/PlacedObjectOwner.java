package ar.edu.utn.frba.proyecto.sigo.domain;


import javax.persistence.*;
import java.util.List;
import lombok.*;


@Entity
@Table(name = "public.tbl_placed_object_owner")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class PlacedObjectOwner {
    @Id
    @SequenceGenerator(name = "placedObjectOwnerGenerator", sequenceName = "PLACED_OBJECT_OWNER_SEQUENCE")
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

}