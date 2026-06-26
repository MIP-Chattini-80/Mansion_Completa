package com.mansion_2.personajes.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personaje")
public class Personaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 80, message = "El nombre debe tener entre 3 y 80 caracteres")
    private String nombre;

    @NotBlank(message = "El origen no puede estar vacío")
    @Size(min = 3, max = 80, message = "El origen debe tener entre 3 y 80 caracteres")
    private String origen;

    @NotBlank(message = "El tipo de origen no puede estar vacío")
    @Size(min = 3, max = 50, message = "El tipo de origen debe tener entre 3 y 50 caracteres")
    private String tipoOrigen;

    @NotNull(message = "El personaje debe tener una categoría asignada")
    @ManyToOne
    @JoinColumn(name = "personajes_id")
    private Personajes personajes;
}
