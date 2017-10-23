package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import javax.persistence.*;
import java.util.List;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import com.google.common.base.MoreObjects;
import lombok.*;

@Entity
@Table(name = "public.tbl_states")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class State extends SigoDomain {
    @Id
    @SequenceGenerator(name = "stateGenerator", sequenceName = "STATE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stateGenerator")

    @Column(name = "state_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code" , length=3)
    private String code;


    @OneToMany(mappedBy = "state", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("number ASC")
    private List<Region> regions;


    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name:", name)
                .add("code", code)
                .toString();
    }
}