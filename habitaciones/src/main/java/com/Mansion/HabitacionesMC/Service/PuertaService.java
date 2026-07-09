package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.DTO.PuertaDTO;
import com.Mansion.HabitacionesMC.Model.Puerta;
import com.Mansion.HabitacionesMC.Repository.PuertaRepository;
import com.Mansion.HabitacionesMC.Validation.PuertaValidaciones;

@Transactional
@Service
public class PuertaService {

    @Autowired
    private PuertaRepository puertaRepository;

    @Autowired
    private PuertaValidaciones puertaValidaciones;

    private PuertaDTO mapToDTO(Puerta puerta) {
        if (puerta == null)
            return null;

        PuertaDTO dto = new PuertaDTO();
        dto.setIdPuerta(puerta.getIdPuerta());
        dto.setEstaBloqueada(puerta.isEstaBloqueada());
        if (puerta.getOrigen() != null) {
            dto.setIdHabitacionOrigen(puerta.getOrigen().getIdHabitacion());
        }
        if (puerta.getDestino() != null) {
            dto.setIdHabitacionDestino(puerta.getDestino().getIdHabitacion());
        }
        return dto;
    }

    public List<PuertaDTO> listarTodas() {
        return puertaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PuertaDTO obtenerPorId(Long id) {
        if (puertaValidaciones.existeEnBaseDatos(id) == false) { /* otra forma es (!puertasValidaciones) */
            throw new RuntimeException("Puerta no encontrada. ");
        }
        return puertaRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public PuertaDTO guardarPuerta(Puerta puerta) {
        if (puertaValidaciones.validarNullSinNada(puerta) == false) {
            throw new IllegalArgumentException("La puerta o sus habitaciones asociadas no pueden ser nulos.");
        }
        if (puerta.getOrigen().getIdHabitacion().equals(puerta.getDestino().getIdHabitacion())) {
            throw new IllegalArgumentException("La habitación de origen y destino no pueden ser la misma.");
        }
        Puerta guardada = puertaRepository.save(puerta);
        return mapToDTO(guardada);
    }

    public PuertaDTO actualizarPuerta(Long id, Puerta PuertaNueva) {
        if (puertaValidaciones.existeEnBaseDatos(id) == false) { /* verifica la existencia de los datos */
            throw new RuntimeException("No se puede actualizar. La puerta no existe. ");
        }
        if (puertaValidaciones.validarNullSinNada(PuertaNueva) == false) { /* verifica si hay datos nuevos */
            throw new IllegalArgumentException("Las habitaciones de origen y destino son obligatorias.");
        }
        if (PuertaNueva.getOrigen().getIdHabitacion().equals(PuertaNueva.getDestino().getIdHabitacion())) { /*
                                                                                                             * verifica
                                                                                                             * si ya
                                                                                                             * existe la
                                                                                                             * habitacion
                                                                                                             */
            throw new IllegalArgumentException("La habitación de origen y destino no pueden ser idénticas.");
        }
        Puerta puertaExistente = puertaRepository.findById(id).get();
        puertaExistente.setOrigen(PuertaNueva.getOrigen());
        puertaExistente.setDestino(PuertaNueva.getDestino());
        puertaExistente.setEstaBloqueada(PuertaNueva.isEstaBloqueada());
        Puerta actualizada = puertaRepository.save(puertaExistente);
        return mapToDTO(actualizada);
    }

    public PuertaDTO editarPuerta(Long id, PuertaDTO PuertaNueva) {
        if (puertaValidaciones.existeEnBaseDatos(id) == false || PuertaNueva == null) {
            throw new RuntimeException(
                    "No se puede editar. La puerta no existe o los datos son inválidos con el ID: " + id);
        }
        Puerta puerta = puertaRepository.findById(id).get();
        puerta.setEstaBloqueada(PuertaNueva.isEstaBloqueada());

        Puerta editada = puertaRepository.save(puerta);
        return mapToDTO(editada);
    }

    public void eliminar(Long id) {
        if (puertaValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. Puerta no existe. ");
        }
        puertaRepository.deleteById(id);
    }

}
