package com.Mansion.HabitacionesMC.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dialogo")
public class Dialogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dialogo")
    private Long idDialogo;

    @Column(name = "codigo_bloque")
    private String codigoBloque;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "personaje")
    private String personaje;

    @Column(name = "texto")
    private String texto;

    @Column(name = "siguiente_bloque")
    private String siguienteBloque;
}
