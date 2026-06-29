package com.jugador_progreso.jugador_progreso.Modelo;
    
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "JUGADOR")
public class Jugador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jugador")
    private Long idJugador;

    @NotBlank(message = "Username obligatorio")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    // Campo nuevo: nombre del jugador dentro del juego (se asigna durante la partida)
    @Column(name = "nombre_jugador")
    private String nombreJugador;

    @NotBlank(message = "Contraseña obligatoria")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe proporcionar un formato de email válido")
    @Column(name = "email")
    private String email;
    
    @OneToOne(mappedBy = "jugador")
    private Progreso progreso;

}
