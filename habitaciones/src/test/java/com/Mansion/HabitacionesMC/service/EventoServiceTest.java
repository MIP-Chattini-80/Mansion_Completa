package com.Mansion.HabitacionesMC.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.Mansion.HabitacionesMC.DTO.EventoDTO;
import com.Mansion.HabitacionesMC.Model.Evento;
import com.Mansion.HabitacionesMC.Model.Eventos;
import com.Mansion.HabitacionesMC.Model.Habitacion;
import com.Mansion.HabitacionesMC.Repository.EventoRepository;
import com.Mansion.HabitacionesMC.Service.EventoService;
import com.Mansion.HabitacionesMC.Validation.EventoValidaciones;

@SpringBootTest
public class EventoServiceTest {

    @Autowired
    private EventoService eventoService;

    @MockitoBean
    private EventoRepository eventoRepository;

    @MockitoBean
    private EventoValidaciones eventoValidaciones;

    private Evento crearEventoBase() {
        Eventos tipoEvento = new Eventos(1L, "Puzzle", 50);
        Habitacion habitacion = new Habitacion(1L, "Biblioteca",
                "Una sala llena de libros antiguos y polvorientos custodiada por Lisa de Genshin Impact",
                false);
        return new Evento(1L, tipoEvento, habitacion, "Un acertijo tallado en la pared de piedra", false);
    }

    @Test
    public void testListarEventosDTO() {
        when(eventoRepository.findAll()).thenReturn(List.of(crearEventoBase()));

        List<EventoDTO> eventos = eventoService.listarEventosDTO();

        assertNotNull(eventos);
        assertEquals(1, eventos.size());
    }

    @Test
    public void testBuscarPorId() {
        when(eventoValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(crearEventoBase()));

        EventoDTO evento = eventoService.buscarPorId(1L);

        assertNotNull(evento);
        assertEquals("Un acertijo tallado en la pared de piedra", evento.getDescripcionEspecifica());
    }

    @Test
    public void testCompletarEvento() {
        Evento existente = crearEventoBase();
        when(eventoValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(eventoRepository.save(existente)).thenReturn(existente);

        EventoDTO completado = eventoService.completarEvento(1L);

        assertNotNull(completado);
        assertTrue(completado.isCompletado());
    }

    @Test
    public void testGuardarEvento() {
        Evento evento = crearEventoBase();
        when(eventoRepository.save(evento)).thenReturn(evento);

        EventoDTO guardado = eventoService.guardarEvento(evento);

        assertNotNull(guardado);
        assertEquals("Un acertijo tallado en la pared de piedra", guardado.getDescripcionEspecifica());
    }

    @Test
    public void testActualizarEvento() {
        Evento existente = crearEventoBase();
        Evento datosNuevos = new Evento(null, existente.getTipoEvento(), existente.getHabitacion(),
                "Una trampilla oculta bajo la alfombra", true);

        when(eventoValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(eventoRepository.save(existente)).thenReturn(existente);

        EventoDTO actualizado = eventoService.actualizarEvento(1L, datosNuevos);

        assertNotNull(actualizado);
        assertEquals("Una trampilla oculta bajo la alfombra", actualizado.getDescripcionEspecifica());
        assertTrue(actualizado.isCompletado());
    }

    @Test
    public void testEditarEvento() {
        Evento existente = crearEventoBase();
        Evento patchData = new Evento();
        patchData.setCompletado(true);

        when(eventoValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(eventoRepository.save(existente)).thenReturn(existente);

        EventoDTO editado = eventoService.editarEvento(1L, patchData);

        assertNotNull(editado);
        assertTrue(editado.isCompletado());
        assertEquals("Un acertijo tallado en la pared de piedra", editado.getDescripcionEspecifica());
    }

    @Test
    public void testEliminar() {
        when(eventoValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        doNothing().when(eventoRepository).deleteById(1L);

        eventoService.eliminar(1L);

        verify(eventoRepository, times(1)).deleteById(1L);
    }

}