package com.Mansion.HabitacionesMC.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Mansion.HabitacionesMC.DTO.ObjetosDTO;
import com.Mansion.HabitacionesMC.Model.Objetos;
import com.Mansion.HabitacionesMC.Repository.ObjetosRepository;
import com.Mansion.HabitacionesMC.Validation.ObjetosValidaciones;

@Transactional
@Service
public class ObjetosService {

    @Autowired
    private ObjetosRepository objetosRepository;

    @Autowired
    private ObjetosValidaciones objetosValidaciones;

    private ObjetosDTO mapToDTO(Objetos modelo) {
        if (modelo == null) return null;
        
        ObjetosDTO dto = new ObjetosDTO();
        dto.setIdInstancia(modelo.getIdInstancia());
        if (modelo.getObjetoBase() != null) {
            dto.setNombreObjeto(modelo.getObjetoBase().getNombre());
        }
        if (modelo.getUbicacion() != null) {
            dto.setNombreHabitacion(modelo.getUbicacion().getNombre());
        }
        dto.setEstado(modelo.getEstado());
        dto.setCantidad(modelo.getCantidad());
        return dto;
    }

    public List<ObjetosDTO> listarTodo() {
        return objetosRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ObjetosDTO obtenerPorId(Long id) {
        if (objetosValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("Instancia física del objeto no encontrada con el ID: " + id);
        }
        return objetosRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public List<ObjetosDTO> buscarPorHabitacion(Long idHabitacion) {
        if (idHabitacion == null) {
            throw new IllegalArgumentException("El ID de la habitación no puede ser nulo.");
        }
        return objetosRepository.findByUbicacion_IdHabitacion(idHabitacion).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ObjetosDTO guardar(Objetos objetitos) {
        if (objetosValidaciones.validarNullSinNada(objetitos) == false) {
            throw new IllegalArgumentException("Los datos del objeto no pueden ser nulos o están incompletos.");
        }
        if (objetitos.getEstado() != null) {
            objetitos.setEstado(objetitos.getEstado().trim().replaceAll("\\s+", " "));
        }

        Objetos guardado = objetosRepository.save(objetitos);
        return mapToDTO(guardado);
    }

    public ObjetosDTO actualizar(Long id, ObjetosDTO objetosNuevos) {
        if (objetosValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede actualizar. La instancia no existe.");
        }
        if (objetosNuevos == null || objetosNuevos.getEstado() == null || objetosNuevos.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio para una actualización.");
        }
        
        if (objetosNuevos.getCantidad() == null || objetosNuevos.getCantidad() < 0) {
            throw new IllegalArgumentException("La cantidad es requerida y no puede ser menor a 0.");
        }
        Objetos existente = objetosRepository.findById(id).get();
        
        existente.setEstado(objetosNuevos.getEstado().trim().replaceAll("\\s+", " "));
        existente.setCantidad(objetosNuevos.getCantidad());

        Objetos actualizado = objetosRepository.save(existente);
        return mapToDTO(actualizado);
    }

    public ObjetosDTO editar(Long id, ObjetosDTO datosNuevos) {
        if (objetosValidaciones.existeEnBaseDatos(id) == false || datosNuevos == null) {
            throw new RuntimeException("No se puede editar. La instancia no existe. ");
        }
        Objetos existente = objetosRepository.findById(id).get();
        if (datosNuevos.getEstado() != null && datosNuevos.getEstado().trim().isEmpty() == false) {
            existente.setEstado(datosNuevos.getEstado().trim().replaceAll("\\s+", " "));
        }
        if (datosNuevos.getCantidad() != null) {
            existente.setCantidad(datosNuevos.getCantidad());
        }
        
        Objetos actualizado = objetosRepository.save(existente); /* aqui se guarda toda la edicion */
        return mapToDTO(actualizado);
    }

    public void eliminar(Long id) {
        if (objetosValidaciones.existeEnBaseDatos(id) == false) {
            throw new RuntimeException("No se puede eliminar. La instancia de objeto especificada no existe.");
        }
        objetosRepository.deleteById(id);
    }
    
}
