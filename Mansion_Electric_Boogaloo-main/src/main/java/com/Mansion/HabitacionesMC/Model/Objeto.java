package com.Mansion.HabitacionesMC.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
@Table(name = "Objeto")
public class Objeto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idObjeto;

    @NotBlank(message = "El nombre del objeto es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre del objeto debe tener entre {min} y {max} caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es necesaria")
    @Size(min = 5, max = 255, message = "La descripción debe tener entre {min} y {max} caracteres")
    private String descripcion;

    @NotBlank(message = "Categoría de objeto obligatoria")
    @Size(min = 3, max = 15, message = "El tipo de objeto debe tener entre {min} y {max} caracteres")
    private String tipoObjeto;
    
    @NotNull(message = "El valor base debe ser definido")
    @Min(value = 0, message = "El valor base no puede ser negativo")
    private Integer valorBase;

}
