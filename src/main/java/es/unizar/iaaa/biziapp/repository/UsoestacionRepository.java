package es.unizar.iaaa.biziapp.repository;

import es.unizar.iaaa.biziapp.domain.Usoestacion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Usoestacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsoestacionRepository extends JpaRepository<Usoestacion,Long> {
    
}
