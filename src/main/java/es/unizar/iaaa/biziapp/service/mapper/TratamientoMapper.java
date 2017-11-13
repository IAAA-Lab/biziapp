package es.unizar.iaaa.biziapp.service.mapper;

import es.unizar.iaaa.biziapp.domain.*;
import es.unizar.iaaa.biziapp.service.dto.TratamientoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tratamiento and its DTO TratamientoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TratamientoMapper extends EntityMapper <TratamientoDTO, Tratamiento> {
    
    
    default Tratamiento fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setId(id);
        return tratamiento;
    }
}
