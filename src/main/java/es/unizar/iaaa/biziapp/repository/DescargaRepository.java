package es.unizar.iaaa.biziapp.repository;

import es.unizar.iaaa.biziapp.domain.Descarga;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Descarga entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescargaRepository extends JpaRepository<Descarga,Long> {
    
}
