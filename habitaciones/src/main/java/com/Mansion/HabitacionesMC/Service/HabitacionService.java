package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.DTO.HabitacionDTO;
import com.Mansion.HabitacionesMC.Model.Habitacion;
import com.Mansion.HabitacionesMC.Repository.HabitacionRepository;
import com.Mansion.HabitacionesMC.Validation.HabitacionValidaciones;

@Transactional
@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private HabitacionValidaciones habitacionValidaciones;

    private HabitacionDTO mapToDTO(Habitacion habitacion) {
        if (habitacion == null)
            return null;

        HabitacionDTO dto = new HabitacionDTO();
        dto.setIdHabitacion(habitacion.getIdHabitacion());
        dto.setNombre(habitacion.getNombre());
        dto.setDescripcion(habitacion.getDescripcion());
        dto.setEsZonaSegura(habitacion.isEsZonaSegura());
        return dto;
    }

    public List<HabitacionDTO> listarTodas() {
        return habitacionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HabitacionDTO obtenerPorId(Long id) {
        if (habitacionValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("Habitación no encontrada");
        }
        return mapToDTO(habitacionRepository.findById(id).get());
    }

    public HabitacionDTO buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la habitación a buscar no puede estar vacío.");
        }
        Habitacion habitacion = habitacionRepository.findByNombreIgnoreCase(nombre.trim());
        if (habitacion == null) {
            throw new RuntimeException("No se encontró ninguna habitación con el nombre: " + nombre);
        }
        return mapToDTO(habitacion);
    }

    public HabitacionDTO guardarHabitacion(Habitacion habitacion) {
        if (habitacionValidaciones.validarNullSinNada(habitacion) == false) {
            throw new IllegalArgumentException("Los datos de la habitación no pueden ser nulos o están incompletos.");
        }

        String nombreSanitizado = habitacion.getNombre().trim().replaceAll("\\s+", " ");
        Habitacion existente = habitacionRepository.findByNombreIgnoreCase(nombreSanitizado);
        if (existente != null) {
            throw new RuntimeException("Ya existe una habitación registrada con el nombre: '" + nombreSanitizado + "'");
        }

        habitacion.setNombre(nombreSanitizado);
        if (habitacion.getDescripcion() != null) {
            habitacion.setDescripcion(habitacion.getDescripcion().trim().replaceAll("\\s+", " "));
        }

        Habitacion guardada = habitacionRepository.save(habitacion);
        return mapToDTO(guardada);
    }

    public HabitacionDTO actualizarHabitacion(Long id, HabitacionDTO nuevaHabitacion) {
        if (habitacionValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede actualizar. La habitación no existe. ");
        }
        if (nuevaHabitacion == null || nuevaHabitacion.getNombre() == null
                || nuevaHabitacion.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede quedar vacío al actualizar.");
        }
        if (nuevaHabitacion.getDescripcion() == null || nuevaHabitacion.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es requerida y no puede quedar vacía.");
        }
        Habitacion habitacionExistente = habitacionRepository.findById(id).get();
        String nuevoNombre = nuevaHabitacion.getNombre().trim().replaceAll("\\s+", " ");
        if (nuevoNombre.equalsIgnoreCase(habitacionExistente.getNombre()) == false) {
            Habitacion duplicado = habitacionRepository.findByNombreIgnoreCase(nuevoNombre);
            if (duplicado != null) {
                throw new RuntimeException(
                        "No se puede actualizar. Ya existe otra habitación llamada: '" + nuevoNombre + "'");
            }
        }
        habitacionExistente.setNombre(nuevoNombre);
        habitacionExistente.setDescripcion(nuevaHabitacion.getDescripcion().trim().replaceAll("\\s+", " "));
        habitacionExistente.setEsZonaSegura(nuevaHabitacion.isEsZonaSegura());
        Habitacion actualizada = habitacionRepository.save(habitacionExistente);
        return mapToDTO(actualizada);
    }

    public HabitacionDTO editarHabitacion(Long id, HabitacionDTO habita) {
        if (habitacionValidaciones.existeEnBaseDatos(id) == false || habita == null) {
            throw new RuntimeException("No se puede editar. La habitación no existe con el ID: " + id);
        }
        Habitacion habitacion = habitacionRepository.findById(id).get();
        if (habita.getNombre() != null && habita.getNombre().trim().isEmpty() == false) {
            String nombreClean = habita.getNombre().trim().replaceAll("\\s+", " ");
            if (nombreClean.equalsIgnoreCase(habitacion.getNombre()) == false) {
                Habitacion duplicado = habitacionRepository.findByNombreIgnoreCase(nombreClean);
                if (duplicado != null) {
                    throw new RuntimeException("Ya existe otra habitación con el nombre: '" + nombreClean + "'");
                }
            }
            habitacion.setNombre(nombreClean);
        }
        if (habita.getDescripcion() != null && habita.getDescripcion().trim().isEmpty() == false) {
            String descClean = habita.getDescripcion().trim().replaceAll("\\s+", " ");
            habitacion.setDescripcion(descClean);
        }
        habitacion.setEsZonaSegura(habita.isEsZonaSegura());
        Habitacion editada = habitacionRepository.save(habitacion);
        return mapToDTO(editada);
    }

    public void eliminar(Long id) {
        if (habitacionValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. Habitación no encontrada con el ID: " + id);
        }
        habitacionRepository.deleteById(id);
    }

}
