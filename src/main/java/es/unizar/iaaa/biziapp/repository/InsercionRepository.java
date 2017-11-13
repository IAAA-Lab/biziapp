package es.unizar.iaaa.biziapp.repository;

import es.unizar.iaaa.biziapp.domain.Insercion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Insercion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsercionRepository extends JpaRepository<Insercion,Long> {
    
}
