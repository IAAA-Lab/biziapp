package es.unizar.iaaa.biziapp.service.mapper;

import es.unizar.iaaa.biziapp.domain.*;
import es.unizar.iaaa.biziapp.service.dto.UsoestacionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Usoestacion and its DTO UsoestacionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UsoestacionMapper extends EntityMapper <UsoestacionDTO, Usoestacion> {
    
    
    default Usoestacion fromId(Long id) {
        if (id == null) {
            return null;
        }
        Usoestacion usoestacion = new Usoestacion();
        usoestacion.setId(id);
        return usoestacion;
    }
}
