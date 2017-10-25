package ar.edu.utn.frba.proyecto.sigo.domain.regulation;

import java.util.List;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class Regulation extends SigoDomain {
    private Long id;
    private String authority;
    private String name;
    private String description;
}