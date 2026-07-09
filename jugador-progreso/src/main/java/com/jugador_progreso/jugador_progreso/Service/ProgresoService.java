package com.jugador_progreso.jugador_progreso.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jugador_progreso.jugador_progreso.DTO.ProgresoDTO;
import com.jugador_progreso.jugador_progreso.Modelo.Progreso;
import com.jugador_progreso.jugador_progreso.Repository.ProgresoRepository;
import com.jugador_progreso.jugador_progreso.Validation.ProgresoValidaciones;

@Transactional
@Service
public class ProgresoService {

    @Autowired
    private ProgresoRepository progresoRepository;

    @Autowired
    private ProgresoValidaciones progresoValidaciones;

    public List<ProgresoDTO> listarTodos() {
        return progresoRepository.findAll().stream()
                .map(progresoValidaciones::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProgresoDTO obtenerPorId(Long id) {
        if (progresoValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("Progreso no encontrado con el ID: " + id);
        }
        Progreso progreso = progresoRepository.findById(id).get();
        return progresoValidaciones.convertirADTO(progreso);
    }

    public ProgresoDTO guardarProgreso(Progreso nuevoProgreso) {
        if (progresoValidaciones.validarNullSinNada(nuevoProgreso) == false) {
            throw new IllegalArgumentException("Los datos del progreso no son válidos o están incompletos.");
        }
        Progreso guardado = progresoRepository.save(nuevoProgreso);
        return progresoValidaciones.convertirADTO(guardado);
    }

    public ProgresoDTO actualizarProgreso(Long id, ProgresoDTO datosNuevos) {
        if (progresoValidaciones.existeEnBaseDatos(id) == false || datosNuevos == null) {
            throw new RuntimeException("No se puede actualizar. El registro de progreso no existe con el ID: " + id);
        }
        if (datosNuevos.getNivelActual() < 1 || datosNuevos.getPuntosExperiencia() < 0) {
            throw new IllegalArgumentException("Valores de nivel o experiencia fuera de los rangos válidos.");
        }
        Progreso existente = progresoRepository.findById(id).get();

        existente.setNivelActual(datosNuevos.getNivelActual());
        existente.setPuntosExperiencia(datosNuevos.getPuntosExperiencia());

        Progreso actualizado = progresoRepository.save(existente);
        return progresoValidaciones.convertirADTO(actualizado);
    }

    public ProgresoDTO editarProgreso(Long id, ProgresoDTO datosNuevos) {
        if (progresoValidaciones.existeEnBaseDatos(id) == false || datosNuevos == null) {
            throw new RuntimeException("No se puede editar. El registro de progreso no existe con el ID: " + id);
        }

        Progreso existente = progresoRepository.findById(id).get();
        if (datosNuevos.getNivelActual() >= 1) {
            existente.setNivelActual(datosNuevos.getNivelActual());
        }
        if (datosNuevos.getPuntosExperiencia() >= 0) {
            existente.setPuntosExperiencia(datosNuevos.getPuntosExperiencia());
        }
        Progreso editado = progresoRepository.save(existente);
        return progresoValidaciones.convertirADTO(editado);
    }

    public void eliminar(Long id) {
        if (progresoValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. El registro de progreso no existe con el ID: " + id);
        }
        progresoRepository.deleteById(id);
    }

}