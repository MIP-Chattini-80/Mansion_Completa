package com.Mansion.HabitacionesMC.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Objetos")
public class Objetos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInstancia;

    @ManyToOne
    @JoinColumn(name = "id_objeto")
    private Objeto objetoBase;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion ubicacion;

    @NotBlank(message = "El estado del objeto (ej: Activo) es obligatorio")
    @Size(min = 3, max = 10, message = "El estado del objeto debe tener entre {min} y {max} caracteres")
    private String estado;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    public Long getIdObjeto() {
        return (this.objetoBase != null) ? this.objetoBase.getIdObjeto() : null;
    }

}
