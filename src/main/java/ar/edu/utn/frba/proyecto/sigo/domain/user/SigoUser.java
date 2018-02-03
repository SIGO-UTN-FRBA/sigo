package ar.edu.utn.frba.proyecto.sigo.domain.user;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "public.tbl_users")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SigoUser extends SigoDomain<String> {

    @Id
    @Column(name = "user_id")
    private String id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String nickname;
}
