package com.mansion_2.personajes.dto;

import lombok.Data;

@Data
public class PersonajeDTO {

    private Long id;
    private String nombre;
    private String origen;
    private String tipoOrigen;
    private Long idPersonajesPersonajes;

}