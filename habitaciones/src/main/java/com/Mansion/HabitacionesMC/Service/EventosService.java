package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.DTO.EventosDTO;
import com.Mansion.HabitacionesMC.Model.Eventos;
import com.Mansion.HabitacionesMC.Repository.EventosRepository;
import com.Mansion.HabitacionesMC.Validation.EventosValidaciones;


@Transactional
@Service
public class EventosService {

    @Autowired
    private EventosRepository eventosRepository;

    @Autowired
    private EventosValidaciones eventosValidaciones;

    private EventosDTO mapToDTO(Eventos modelo) {
        if (modelo == null) return null;

        EventosDTO dto = new EventosDTO();
        dto.setIdEventos(modelo.getIdEventos());
        dto.setCategoria(modelo.getCategoria());
        dto.setExperienciaBase(modelo.getExperienciaBase());
        return dto;
    }

    public List<EventosDTO> listarCategorias() {
        return eventosRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public EventosDTO obtenerPorId(Long id) {
        if (eventosValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("Instancia de evento no encontrada");
        }
        return mapToDTO(eventosRepository.findById(id).get());
    }

    public EventosDTO guardarInstancia(Eventos eventos) {
        if (eventosValidaciones.validarNullSinNada(eventos) == false) {
            throw new IllegalArgumentException("Los datos de la categoría no pueden ser nulos o están incompletos.");
        }
        String categoriaSanitizada = eventos.getCategoria().trim().replaceAll("\\s+", " ");
        Eventos existente = eventosRepository.findByCategoriaIgnoreCase(categoriaSanitizada);
        if (existente != null) {
            throw new RuntimeException("Ya existe un registro con la categoría: '" + categoriaSanitizada + "'");
        }
        eventos.setCategoria(categoriaSanitizada);
        Eventos guardado = eventosRepository.save(eventos);
        return mapToDTO(guardado);
    }

    public EventosDTO actualizarInstancia(Long id, EventosDTO eventitos) {
        if (eventosValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede actualizar. La categoría no existe con el ID: " + id);
        }
        if (eventitos == null || eventitos.getCategoria() == null || eventitos.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria para una actualización completa (PUT).");
        }

        Eventos existente = eventosRepository.findById(id).get();
        String categoriaSanitizada = eventitos.getCategoria().trim().replaceAll("\\s+", " ");

        if (existente.getCategoria().equalsIgnoreCase(categoriaSanitizada) == false) {
            Eventos duplicado = eventosRepository.findByCategoriaIgnoreCase(categoriaSanitizada);
            if (duplicado != null) {
                throw new RuntimeException("Ya existe otra categoría con el nombre: '" + categoriaSanitizada + "'");
            }
        }

        existente.setCategoria(categoriaSanitizada);
        if (eventitos.getExperienciaBase() >= 0) {
            existente.setExperienciaBase(eventitos.getExperienciaBase());
        }

        Eventos actualizado = eventosRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public EventosDTO editar(Long id, EventosDTO eventitos) {
        if (eventosValidaciones.existeEnBaseDatos(id) == false || eventitos == null) {
            throw new RuntimeException("No se puede editar. La categoría no existe con el ID: " + id);
        }
        Eventos existente = eventosRepository.findById(id).get();
        if (eventitos.getCategoria() != null && eventitos.getCategoria().trim().isEmpty() == false) {
            String categoriaSanitizada = eventitos.getCategoria().trim().replaceAll("\\s+", " ");

            if (existente.getCategoria().equalsIgnoreCase(categoriaSanitizada) == false) {
                Eventos duplicado = eventosRepository.findByCategoriaIgnoreCase(categoriaSanitizada);
                if (duplicado != null) {
                    throw new RuntimeException("Ya existe otra categoría con el nombre: '" + categoriaSanitizada + "'");
                }
            }
            existente.setCategoria(categoriaSanitizada);
        }
        if (eventitos.getExperienciaBase() >= 0) {
            existente.setExperienciaBase(eventitos.getExperienciaBase());
        }
        Eventos actualizado = eventosRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public void eliminar(Long id) {
        if (eventosValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. La categoría de evento especificada no existe con el ID: " + id);
        }
        eventosRepository.deleteById(id);
    }

}
