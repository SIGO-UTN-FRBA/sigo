package ar.edu.utn.frba.proyecto.sigo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionDTO {

    private String code;
    private String description;
}