package com.jugador_progreso.jugador_progreso.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.jugador_progreso.jugador_progreso.DTO.JugadorDTO;
import com.jugador_progreso.jugador_progreso.Modelo.Jugador;
import com.jugador_progreso.jugador_progreso.Repository.JugadorRepository;
import com.jugador_progreso.jugador_progreso.Validation.JugadorValidaciones;

@SpringBootTest
public class JugadorServiceTest {

    @Autowired
    private JugadorService jugadorService;

    @MockitoBean
    private JugadorRepository jugadorRepository;

    @MockitoBean
    private JugadorValidaciones jugadorValidaciones;

    private Jugador crearJugadorBase() {
        return new Jugador(1L, "RaoraLover600", "El Más Cabrón", "123456", "RaoraLover600@mail.com", null);
    }

    @Test
    public void testListarTodos() {
        when(jugadorRepository.findAll()).thenReturn(List.of(crearJugadorBase()));

        List<JugadorDTO> resultado = jugadorService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testObtenerPorId() {
        when(jugadorValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(crearJugadorBase()));

        JugadorDTO dto = jugadorService.obtenerPorId(1L);

        assertNotNull(dto);
        assertEquals("RaoraLover600", dto.getUsername());
    }

    @Test
    public void testGuardarJugador() {
        Jugador jugador = crearJugadorBase();
        when(jugadorValidaciones.validarNullSinNada(jugador)).thenReturn(true);
        when(jugadorRepository.save(jugador)).thenReturn(jugador);

        JugadorDTO guardado = jugadorService.guardarJugador(jugador);

        assertNotNull(guardado);
        assertEquals("RaoraLover600", guardado.getUsername());
    }

    @Test
    public void testEliminar() {
        when(jugadorValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        doNothing().when(jugadorRepository).deleteById(1L);

        jugadorService.eliminar(1L);

        verify(jugadorRepository, times(1)).deleteById(1L);
    }

}