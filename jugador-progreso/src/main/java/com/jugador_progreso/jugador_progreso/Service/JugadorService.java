package com.jugador_progreso.jugador_progreso.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jugador_progreso.jugador_progreso.DTO.JugadorDTO;
import com.jugador_progreso.jugador_progreso.Modelo.Jugador;
import com.jugador_progreso.jugador_progreso.Repository.JugadorRepository;
import com.jugador_progreso.jugador_progreso.Validation.JugadorValidaciones;

@Transactional
@Service
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private JugadorValidaciones jugadorValidaciones;

    private JugadorDTO mapToDTO(Jugador jugador) {
        if (jugador == null) return null;

        JugadorDTO dto = new JugadorDTO();
        dto.setIdJugador(jugador.getIdJugador());
        dto.setUsername(jugador.getUsername());
        dto.setEmail(jugador.getEmail());
        return dto;
    }

    public List<JugadorDTO> listarTodos() {
        return jugadorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public JugadorDTO obtenerPorId(Long id) {
        if (jugadorValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("Jugador no encontrado con el ID: " + id);
        }
        return mapToDTO(jugadorRepository.findById(id).get());
    }

    public JugadorDTO guardarJugador(Jugador jugador) {
        if (jugadorValidaciones.validarNullSinNada(jugador) == false) {
            throw new IllegalArgumentException("Los datos del jugador no son válidos o están incompletos.");
        }
        Jugador guardado = jugadorRepository.save(jugador);
        return mapToDTO(guardado);
    }

    public JugadorDTO actualizarJugador(Long id, JugadorDTO datosNuevos) {
        if (jugadorValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede actualizar. El jugador no existe con el ID: " + id);
        }
        if (datosNuevos == null) {
            throw new IllegalArgumentException("Los datos de actualización no pueden ser nulos.");
        }
        if (datosNuevos.getUsername() == null || datosNuevos.getUsername().trim().isEmpty() ||
            datosNuevos.getEmail() == null || datosNuevos.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El username y el email son campos obligatorios para la actualización.");
        }

        Jugador existente = jugadorRepository.findById(id).get();
        existente.setUsername(datosNuevos.getUsername());
        existente.setEmail(datosNuevos.getEmail());

        Jugador actualizado = jugadorRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public JugadorDTO editarJugador(Long id, JugadorDTO datosNuevos) {
        if (jugadorValidaciones.existeEnBaseDatos(id) == false || datosNuevos == null) {
            throw new RuntimeException("No se puede editar. El jugador no existe con el ID: " + id);
        }

        Jugador existente = jugadorRepository.findById(id).get();

        if (datosNuevos.getUsername() != null && !datosNuevos.getUsername().trim().isEmpty()) {
            existente.setUsername(datosNuevos.getUsername());
        }
        if (datosNuevos.getEmail() != null && !datosNuevos.getEmail().trim().isEmpty()) {
            existente.setEmail(datosNuevos.getEmail());
        }

        Jugador editado = jugadorRepository.save(existente);
        return mapToDTO(editado);
    }

    public void eliminar(Long id) {
        if (jugadorValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. El jugador no existe con el ID: " + id);
        }
        jugadorRepository.deleteById(id);
    }

}