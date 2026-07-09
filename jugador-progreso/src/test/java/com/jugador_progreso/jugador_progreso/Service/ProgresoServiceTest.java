package com.jugador_progreso.jugador_progreso.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

import com.jugador_progreso.jugador_progreso.DTO.ProgresoDTO;
import com.jugador_progreso.jugador_progreso.Modelo.Jugador;
import com.jugador_progreso.jugador_progreso.Modelo.Progreso;
import com.jugador_progreso.jugador_progreso.Repository.ProgresoRepository;
import com.jugador_progreso.jugador_progreso.Validation.ProgresoValidaciones;

@SpringBootTest
public class ProgresoServiceTest {

    @Autowired
    private ProgresoService progresoService;

    @MockitoBean
    private ProgresoRepository progresoRepository;

    @MockitoBean
    private ProgresoValidaciones progresoValidaciones;

    private Progreso crearProgresoBase() {
        Jugador jugador = new Jugador(1L, "RaoraLover600", "El Más Cabrón", "123456", "RaoraLover600@mail.com", null);
        return new Progreso(1L, jugador, 5, 1200, 2L, 3L);
    }

    private ProgresoDTO crearProgresoDTOBase() {
        ProgresoDTO dto = new ProgresoDTO();
        dto.setIdProgreso(1L);
        dto.setIdJugador(1L);
        dto.setNivelActual(5);
        dto.setPuntosExperiencia(1200);
        dto.setNombreHabitacionActual("Biblioteca");
        dto.setNombreUltimoPersonaje("Satoru Gojo");
        return dto;
    }

    @Test
    public void testListarTodos() {
        when(progresoRepository.findAll()).thenReturn(List.of(crearProgresoBase()));
        when(progresoValidaciones.convertirADTO(any(Progreso.class))).thenReturn(crearProgresoDTOBase());

        List<ProgresoDTO> resultado = progresoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testObtenerPorId() {
        when(progresoValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(progresoRepository.findById(1L)).thenReturn(Optional.of(crearProgresoBase()));
        when(progresoValidaciones.convertirADTO(any(Progreso.class))).thenReturn(crearProgresoDTOBase());

        ProgresoDTO dto = progresoService.obtenerPorId(1L);

        assertNotNull(dto);
        assertEquals(5, dto.getNivelActual());
    }

    @Test
    public void testGuardarProgreso() {
        Progreso progreso = crearProgresoBase();
        when(progresoValidaciones.validarNullSinNada(progreso)).thenReturn(true);
        when(progresoRepository.save(progreso)).thenReturn(progreso);
        when(progresoValidaciones.convertirADTO(progreso)).thenReturn(crearProgresoDTOBase());

        ProgresoDTO guardado = progresoService.guardarProgreso(progreso);

        assertNotNull(guardado);
        assertEquals(1200, guardado.getPuntosExperiencia());
    }

    @Test
    public void testActualizarProgreso() {
        Progreso existente = crearProgresoBase();
        ProgresoDTO datosNuevos = new ProgresoDTO();
        datosNuevos.setNivelActual(10);
        datosNuevos.setPuntosExperiencia(5000);

        ProgresoDTO dtoActualizado = crearProgresoDTOBase();
        dtoActualizado.setNivelActual(10);
        dtoActualizado.setPuntosExperiencia(5000);

        when(progresoValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(progresoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(progresoRepository.save(existente)).thenReturn(existente);
        when(progresoValidaciones.convertirADTO(existente)).thenReturn(dtoActualizado);

        ProgresoDTO actualizado = progresoService.actualizarProgreso(1L, datosNuevos);

        assertNotNull(actualizado);
        assertEquals(10, actualizado.getNivelActual());
        assertEquals(5000, actualizado.getPuntosExperiencia());
    }

    @Test
    public void testEditarProgreso() {
        Progreso existente = crearProgresoBase();
        ProgresoDTO patchData = new ProgresoDTO();
        patchData.setNivelActual(8);

        ProgresoDTO dtoEditado = crearProgresoDTOBase();
        dtoEditado.setNivelActual(8);
        dtoEditado.setPuntosExperiencia(0);

        when(progresoValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        when(progresoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(progresoRepository.save(existente)).thenReturn(existente);
        when(progresoValidaciones.convertirADTO(existente)).thenReturn(dtoEditado);

        ProgresoDTO editado = progresoService.editarProgreso(1L, patchData);

        assertNotNull(editado);
        assertEquals(8, editado.getNivelActual());
        assertEquals(0, editado.getPuntosExperiencia());
    }

    @Test
    public void testEliminar() {
        when(progresoValidaciones.existeEnBaseDatos(1L)).thenReturn(true);
        doNothing().when(progresoRepository).deleteById(1L);
        progresoService.eliminar(1L);
        verify(progresoRepository, times(1)).deleteById(1L);
    }

}