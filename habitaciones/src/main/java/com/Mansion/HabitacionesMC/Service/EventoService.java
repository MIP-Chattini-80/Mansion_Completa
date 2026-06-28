package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.DTO.EventoDTO;
import com.Mansion.HabitacionesMC.Model.Evento;
import com.Mansion.HabitacionesMC.Repository.EventoRepository;
import com.Mansion.HabitacionesMC.Validation.EventoValidaciones;

@Transactional
@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EventoValidaciones eventoValidaciones;

    private EventoDTO mapToDTO(Evento evento) {
        if (evento == null)
            return null;

        EventoDTO dto = new EventoDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setDescripcionEspecifica(evento.getDescripcionEspecifica());
        dto.setCompletado(evento.isCompletado());
        return dto;
    }

    public List<EventoDTO> listarEventosDTO() {
        return eventoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public EventoDTO buscarPorId(Long id) {
        if (eventoValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("ID " + id + " no está registrado");
        }
        Evento evento = eventoRepository.findById(id).get();
        return mapToDTO(evento);
    }

    public EventoDTO completarEvento(Long id) {
        if (eventoValidaciones.existeEnBaseDatos(id) == false) {
            return null;
        }
        Evento evento = eventoRepository.findById(id).get();
        evento.setCompletado(true);
        return mapToDTO(eventoRepository.save(evento));
    }

    public EventoDTO guardarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo");
        }
        if (evento.getDescripcionEspecifica() != null) {
            String descLimpia = evento.getDescripcionEspecifica().trim().replaceAll("\\s+", " ");
            evento.setDescripcionEspecifica(descLimpia);
        }
        Evento guardado = eventoRepository.save(evento);
        return mapToDTO(guardado);
    }

    public EventoDTO actualizarEvento(Long id, Evento eventoDetalles) {
        if (eventoValidaciones.existeEnBaseDatos(id) == false || eventoDetalles == null) {
            throw new RuntimeException("No existe el evento con el ID: " + id);
        }

        Evento eventoExistente = eventoRepository.findById(id).get();

        if (eventoDetalles.getDescripcionEspecifica() != null
                && eventoDetalles.getDescripcionEspecifica().trim().isEmpty() == false) {
            String descSanitizada = eventoDetalles.getDescripcionEspecifica().trim().replaceAll("\\s+", " ");
            eventoExistente.setDescripcionEspecifica(descSanitizada);
        }
        eventoExistente.setCompletado(eventoDetalles.isCompletado());

        Evento guardado = eventoRepository.save(eventoExistente);
        return mapToDTO(guardado);
    }

    public EventoDTO editarEvento(Long id, Evento eventito) {
        if (eventoValidaciones.existeEnBaseDatos(id) == false || eventito == null) {
            throw new RuntimeException("No existe el evento con el ID: " + id);
        }

        Evento existente = eventoRepository.findById(id).get();

        if (eventito.getTipoEvento() != null) {
            existente.setTipoEvento(eventito.getTipoEvento());
        }
        if (eventito.getHabitacion() != null) {
            existente.setHabitacion(eventito.getHabitacion());
        }
        if (eventito.getDescripcionEspecifica() != null
                && eventito.getDescripcionEspecifica().trim().isEmpty() == false) {
            String descLimpia = eventito.getDescripcionEspecifica().trim().replaceAll("\\s+", " ");
            existente.setDescripcionEspecifica(descLimpia);
        }
        existente.setCompletado(eventito.isCompletado());

        Evento editado = eventoRepository.save(existente);
        return mapToDTO(editado);
    }

    public void eliminar(Long id) {
        if (eventoValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. Evento no encontrado");
        }
        eventoRepository.deleteById(id);
    }

}