package es.unizar.iaaa.biziapp.service.mapper;

import es.unizar.iaaa.biziapp.domain.*;
import es.unizar.iaaa.biziapp.service.dto.DescargaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Descarga and its DTO DescargaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DescargaMapper extends EntityMapper <DescargaDTO, Descarga> {
    
    
    default Descarga fromId(Long id) {
        if (id == null) {
            return null;
        }
        Descarga descarga = new Descarga();
        descarga.setId(id);
        return descarga;
    }
}
