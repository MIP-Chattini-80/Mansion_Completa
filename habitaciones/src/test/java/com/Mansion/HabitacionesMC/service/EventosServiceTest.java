package com.Mansion.HabitacionesMC.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.Mansion.HabitacionesMC.DTO.EventosDTO;
import com.Mansion.HabitacionesMC.Model.Eventos;
import com.Mansion.HabitacionesMC.Repository.EventosRepository;
import com.Mansion.HabitacionesMC.Service.EventosService;
import com.Mansion.HabitacionesMC.Validation.EventosValidaciones;

@SpringBootTest
public class EventosServiceTest {

    @Autowired
    private EventosService eventosService;

    @MockitoBean
    private EventosRepository eventosRepository;

    @MockitoBean
    private EventosValidaciones eventosValidaciones;

    private Eventos crearEventosBase() {
        return new Eventos(1L, "Puzzle", 50);
    }

    @Test
    public void testListarCategorias() {
        when(eventosRepository.findAll()).thenReturn(List.of(crearEventosBase()));

        List<EventosDTO> categorias = eventosService.listarCategorias();

        assertNotNull(categorias);
        assertEquals(1, categorias.size());
    }

    @Test
    public void testObtenerPorId() {
        when(eventosValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(eventosRepository.findById(1L)).thenReturn(Optional.of(crearEventosBase()));

        EventosDTO categoria = eventosService.obtenerPorId(1L);

        assertNotNull(categoria);
        assertEquals("Puzzle", categoria.getCategoria());
    }

    @Test
    public void testGuardarInstancia() {
        Eventos eventos = crearEventosBase();
        when(eventosValidaciones.validarNullSinNada(eventos)).thenReturn(true);
        when(eventosRepository.findByCategoriaIgnoreCase("Puzzle")).thenReturn(null);
        when(eventosRepository.save(eventos)).thenReturn(eventos);

        EventosDTO guardado = eventosService.guardarInstancia(eventos);

        assertNotNull(guardado);
        assertEquals("Puzzle", guardado.getCategoria());
    }

    @Test
    public void testGuardarInstanciaCategoriaDuplicada() {
        Eventos eventos = crearEventosBase();
        when(eventosValidaciones.validarNullSinNada(eventos)).thenReturn(true);
        when(eventosRepository.findByCategoriaIgnoreCase("Puzzle")).thenReturn(crearEventosBase());

        assertThrows(RuntimeException.class, () -> eventosService.guardarInstancia(eventos));
    }

    @Test
    public void testActualizarInstancia() {
        Eventos existente = crearEventosBase();
        EventosDTO datosNuevos = new EventosDTO();
        datosNuevos.setCategoria("Trampa");
        datosNuevos.setExperienciaBase(80);

        when(eventosValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(eventosRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(eventosRepository.findByCategoriaIgnoreCase("Trampa")).thenReturn(null);
        when(eventosRepository.save(existente)).thenReturn(existente);

        EventosDTO actualizado = eventosService.actualizarInstancia(1L, datosNuevos);

        assertNotNull(actualizado);
        assertEquals("Trampa", actualizado.getCategoria());
        assertEquals(80, actualizado.getExperienciaBase());
    }

    @Test
    public void testEditar() {
        Eventos existente = crearEventosBase();
        EventosDTO patchData = new EventosDTO();
        patchData.setExperienciaBase(100);

        when(eventosValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(eventosRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(eventosRepository.save(existente)).thenReturn(existente);

        EventosDTO editado = eventosService.editar(1L, patchData);

        assertNotNull(editado);
        assertEquals("Puzzle", editado.getCategoria());
        assertEquals(100, editado.getExperienciaBase());
    }

    @Test
    public void testEliminar() {
        when(eventosValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        doNothing().when(eventosRepository).deleteById(1L);

        eventosService.eliminar(1L);

        verify(eventosRepository, times(1)).deleteById(1L);
    }

}
