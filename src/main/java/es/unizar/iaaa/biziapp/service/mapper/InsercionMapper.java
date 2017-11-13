package es.unizar.iaaa.biziapp.service.mapper;

import es.unizar.iaaa.biziapp.domain.*;
import es.unizar.iaaa.biziapp.service.dto.InsercionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Insercion and its DTO InsercionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InsercionMapper extends EntityMapper <InsercionDTO, Insercion> {
    
    
    default Insercion fromId(Long id) {
        if (id == null) {
            return null;
        }
        Insercion insercion = new Insercion();
        insercion.setId(id);
        return insercion;
    }
}
