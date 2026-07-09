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

import com.Mansion.HabitacionesMC.DTO.PuertaDTO;
import com.Mansion.HabitacionesMC.Model.Habitacion;
import com.Mansion.HabitacionesMC.Model.Puerta;
import com.Mansion.HabitacionesMC.Repository.PuertaRepository;
import com.Mansion.HabitacionesMC.Service.PuertaService;
import com.Mansion.HabitacionesMC.Validation.PuertaValidaciones;

@SpringBootTest
public class PuertaServiceTest {

    @Autowired
    private PuertaService puertaService;

    @MockitoBean
    private PuertaRepository puertaRepository;

    @MockitoBean
    private PuertaValidaciones puertaValidaciones;

    private Habitacion crearHabitacion(Long id, String nombre) {
        return new Habitacion(id, nombre, "Una habitación cualquiera de la mansión, con su propia historia", false);
    }

    private Puerta crearPuertaBase() {
        return new Puerta(1L, crearHabitacion(1L, "Biblioteca"), crearHabitacion(2L, "Salón de Té"), false);
    }

    @Test
    public void testListarTodas() {
        when(puertaRepository.findAll()).thenReturn(List.of(crearPuertaBase()));

        List<PuertaDTO> puertas = puertaService.listarTodas();

        assertNotNull(puertas);
        assertEquals(1, puertas.size());
    }

    @Test
    public void testObtenerPorId() {
        when(puertaValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(puertaRepository.findById(1L)).thenReturn(Optional.of(crearPuertaBase()));

        PuertaDTO puerta = puertaService.obtenerPorId(1L);

        assertNotNull(puerta);
        assertEquals(1L, puerta.getIdHabitacionOrigen());
        assertEquals(2L, puerta.getIdHabitacionDestino());
    }

    @Test
    public void testGuardarPuerta() {
        Puerta puerta = crearPuertaBase();
        when(puertaValidaciones.validarNullSinNada(puerta)).thenReturn(true);
        when(puertaRepository.save(puerta)).thenReturn(puerta);

        PuertaDTO guardada = puertaService.guardarPuerta(puerta);

        assertNotNull(guardada);
        assertEquals(false, guardada.isEstaBloqueada());
    }

    @Test
    public void testGuardarPuertaMismaHabitacion() {
        Habitacion habitacion = crearHabitacion(1L, "Biblioteca");
        Puerta puerta = new Puerta(null, habitacion, habitacion, false);
        when(puertaValidaciones.validarNullSinNada(puerta)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> puertaService.guardarPuerta(puerta));
    }

    @Test
    public void testActualizarPuerta() {
        Puerta existente = crearPuertaBase();
        Puerta datosNuevos = new Puerta(null, crearHabitacion(3L, "Cocina"), crearHabitacion(4L, "Sótano"), true);

        when(puertaValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(puertaValidaciones.validarNullSinNada(datosNuevos)).thenReturn(true);
        when(puertaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(puertaRepository.save(existente)).thenReturn(existente);

        PuertaDTO actualizada = puertaService.actualizarPuerta(1L, datosNuevos);

        assertNotNull(actualizada);
        assertEquals(3L, actualizada.getIdHabitacionOrigen());
        assertEquals(4L, actualizada.getIdHabitacionDestino());
        assertEquals(true, actualizada.isEstaBloqueada());
    }

    @Test
    public void testEditarPuerta() {
        Puerta existente = crearPuertaBase();
        PuertaDTO patchData = new PuertaDTO();
        patchData.setEstaBloqueada(true);

        when(puertaValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(puertaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(puertaRepository.save(existente)).thenReturn(existente);

        PuertaDTO editada = puertaService.editarPuerta(1L, patchData);

        assertNotNull(editada);
        assertEquals(true, editada.isEstaBloqueada());
    }

    @Test
    public void testEliminar() {
        when(puertaValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        doNothing().when(puertaRepository).deleteById(1L);

        puertaService.eliminar(1L);

        verify(puertaRepository, times(1)).deleteById(1L);
    }

}