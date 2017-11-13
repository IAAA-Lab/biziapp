package es.unizar.iaaa.biziapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.biziapp.domain.Tratamiento;

import es.unizar.iaaa.biziapp.repository.TratamientoRepository;
import es.unizar.iaaa.biziapp.web.rest.util.HeaderUtil;
import es.unizar.iaaa.biziapp.web.rest.util.PaginationUtil;
import es.unizar.iaaa.biziapp.service.dto.TratamientoDTO;
import es.unizar.iaaa.biziapp.service.mapper.TratamientoMapper;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tratamiento.
 */
@RestController
@RequestMapping("/api")
public class TratamientoResource {

    private final Logger log = LoggerFactory.getLogger(TratamientoResource.class);

    private static final String ENTITY_NAME = "tratamiento";

    private final TratamientoRepository tratamientoRepository;

    private final TratamientoMapper tratamientoMapper;

    public TratamientoResource(TratamientoRepository tratamientoRepository, TratamientoMapper tratamientoMapper) {
        this.tratamientoRepository = tratamientoRepository;
        this.tratamientoMapper = tratamientoMapper;
    }

    /**
     * POST  /tratamientos : Create a new tratamiento.
     *
     * @param tratamientoDTO the tratamientoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tratamientoDTO, or with status 400 (Bad Request) if the tratamiento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tratamientos")
    @Timed
    public ResponseEntity<TratamientoDTO> createTratamiento(@Valid @RequestBody TratamientoDTO tratamientoDTO) throws URISyntaxException {
        log.debug("REST request to save Tratamiento : {}", tratamientoDTO);
        if (tratamientoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tratamiento cannot already have an ID")).body(null);
        }
        Tratamiento tratamiento = tratamientoMapper.toEntity(tratamientoDTO);
        tratamiento = tratamientoRepository.save(tratamiento);
        TratamientoDTO result = tratamientoMapper.toDto(tratamiento);
        return ResponseEntity.created(new URI("/api/tratamientos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tratamientos : Updates an existing tratamiento.
     *
     * @param tratamientoDTO the tratamientoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tratamientoDTO,
     * or with status 400 (Bad Request) if the tratamientoDTO is not valid,
     * or with status 500 (Internal Server Error) if the tratamientoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tratamientos")
    @Timed
    public ResponseEntity<TratamientoDTO> updateTratamiento(@Valid @RequestBody TratamientoDTO tratamientoDTO) throws URISyntaxException {
        log.debug("REST request to update Tratamiento : {}", tratamientoDTO);
        if (tratamientoDTO.getId() == null) {
            return createTratamiento(tratamientoDTO);
        }
        Tratamiento tratamiento = tratamientoMapper.toEntity(tratamientoDTO);
        tratamiento = tratamientoRepository.save(tratamiento);
        TratamientoDTO result = tratamientoMapper.toDto(tratamiento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tratamientoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tratamientos : get all the tratamientos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tratamientos in body
     */
    @GetMapping("/tratamientos")
    @Timed
    public ResponseEntity<List<TratamientoDTO>> getAllTratamientos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Tratamientos");
        Page<Tratamiento> page = tratamientoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tratamientos");
        return new ResponseEntity<>(tratamientoMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /tratamientos/:id : get the "id" tratamiento.
     *
     * @param id the id of the tratamientoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tratamientoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tratamientos/{id}")
    @Timed
    public ResponseEntity<TratamientoDTO> getTratamiento(@PathVariable Long id) {
        log.debug("REST request to get Tratamiento : {}", id);
        Tratamiento tratamiento = tratamientoRepository.findOne(id);
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tratamientoDTO));
    }

    /**
     * DELETE  /tratamientos/:id : delete the "id" tratamiento.
     *
     * @param id the id of the tratamientoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tratamientos/{id}")
    @Timed
    public ResponseEntity<Void> deleteTratamiento(@PathVariable Long id) {
        log.debug("REST request to delete Tratamiento : {}", id);
        tratamientoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
