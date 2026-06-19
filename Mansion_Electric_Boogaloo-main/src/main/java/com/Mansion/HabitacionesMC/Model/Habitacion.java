package com.Mansion.HabitacionesMC.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HABITACION")
public class Habitacion {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Long idHabitacion;

    @NotBlank(message = "La habitación debe tener un nombre")
    @Size(min = 2, max = 20, message = "El nombre de la habitación debe tener entre {min} y {max} caracteres.")
    private String nombre;

    @NotBlank(message = "La descripción es necesaria para la ambientación")
    @Size(min = 10, max = 255, message = "La descripción de la ambientación debe tener entre {min} y {max} caracteres.")
    private String descripcion;

    @Column(name = "es_zona_segura")
    private boolean esZonaSegura;

}
