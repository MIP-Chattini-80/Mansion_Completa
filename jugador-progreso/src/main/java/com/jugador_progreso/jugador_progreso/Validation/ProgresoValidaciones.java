package com.jugador_progreso.jugador_progreso.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.jugador_progreso.jugador_progreso.DTO.ProgresoDTO;
import com.jugador_progreso.jugador_progreso.Modelo.Progreso;
import com.jugador_progreso.jugador_progreso.Repository.ProgresoRepository;

import reactor.core.publisher.Mono;

@Component
public class ProgresoValidaciones {

    @Autowired
    private ProgresoRepository progresoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Boolean validarNullSinNada(Progreso progreso) {
        if (progreso == null) {
            return false;
        }
        if (progreso.getJugador() == null || progreso.getJugador().getIdJugador() == null) {
            return false;
        }
        if (progreso.getNivelActual() < 1) {
            return false;
        }
        if (progreso.getPuntosExperiencia() < 0) {
            return false;
        }
        return true;
    }

    public Boolean existeEnBaseDatos(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return progresoRepository.existsById(id);
    }

    public String obtenerNombreHabitacion(Long habitacionId) {
        if (habitacionId == null) {
            return "Vestíbulo Principal";
        }
        try {
            String resultado = webClientBuilder.build()
                    .get()
                    .uri("http://mansion-habitaciones/api/v1/habitaciones/nombre/" + habitacionId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .bodyToMono(String.class)
                    .block();

            if (resultado != null) {
                return resultado;
            }
            return "sin habitación asignada";

        } catch (Exception e) {
            return "no se pudo conectar con el microservicio de habitaciones";
        }
    }

    public String obtenerNombrePersonaje(Long personajeId) {
        if (personajeId == null) {
            return "Ninguno (Explorando solo)";
        }
        try {
            String resultado = webClientBuilder.build()
                    .get()
                    .uri("http://personajes/api/v1/personajes/nombre/" + personajeId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .bodyToMono(String.class)
                    .block();

            if (resultado != null) {
                return resultado;
            }
            return "sin personaje asignado";

        } catch (Exception e) {
            return "no se pudo conectar con el microservicio de personajes";
        }
    }

    public ProgresoDTO convertirADTO(Progreso progreso) {
        if (progreso == null) {
            return null;
        }
        ProgresoDTO dto = new ProgresoDTO();
        dto.setIdProgreso(progreso.getIdProgreso());
        if (progreso.getJugador() != null) {
            dto.setIdJugador(progreso.getJugador().getIdJugador());
        }
        dto.setNivelActual(progreso.getNivelActual());
        dto.setPuntosExperiencia(progreso.getPuntosExperiencia());

        dto.setNombreHabitacionActual(this.obtenerNombreHabitacion(progreso.getHabitacionActualId()));
        dto.setNombreUltimoPersonaje(this.obtenerNombrePersonaje(progreso.getUltimoPersonajeId()));

        return dto;
    }

}