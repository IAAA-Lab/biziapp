package es.unizar.iaaa.biziapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.biziapp.domain.Insercion;

import es.unizar.iaaa.biziapp.repository.InsercionRepository;
import es.unizar.iaaa.biziapp.security.AuthoritiesConstants;
import es.unizar.iaaa.biziapp.web.rest.util.HeaderUtil;
import es.unizar.iaaa.biziapp.web.rest.util.PaginationUtil;
import es.unizar.iaaa.biziapp.service.dto.InsercionDTO;
import es.unizar.iaaa.biziapp.service.mapper.InsercionMapper;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Insercion.
 */
@RestController
@RequestMapping("/api")
public class InsercionResource {

    private final Logger log = LoggerFactory.getLogger(InsercionResource.class);

    private static final String ENTITY_NAME = "insercion";

    private final InsercionRepository insercionRepository;

    private final InsercionMapper insercionMapper;

    public InsercionResource(InsercionRepository insercionRepository, InsercionMapper insercionMapper) {
        this.insercionRepository = insercionRepository;
        this.insercionMapper = insercionMapper;
    }

    /**
     * POST  /insercions : Create a new insercion.
     *
     * @param insercionDTO the insercionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insercionDTO, or with status 400 (Bad Request) if the insercion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/insercions")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<InsercionDTO> createInsercion(@Valid @RequestBody InsercionDTO insercionDTO) throws URISyntaxException {
        log.debug("REST request to save Insercion : {}", insercionDTO);
        if (insercionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new insercion cannot already have an ID")).body(null);
        }
        Insercion insercion = insercionMapper.toEntity(insercionDTO);
        insercion = insercionRepository.save(insercion);
        InsercionDTO result = insercionMapper.toDto(insercion);
        return ResponseEntity.created(new URI("/api/insercions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insercions : Updates an existing insercion.
     *
     * @param insercionDTO the insercionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insercionDTO,
     * or with status 400 (Bad Request) if the insercionDTO is not valid,
     * or with status 500 (Internal Server Error) if the insercionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/insercions")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<InsercionDTO> updateInsercion(@Valid @RequestBody InsercionDTO insercionDTO) throws URISyntaxException {
        log.debug("REST request to update Insercion : {}", insercionDTO);
        if (insercionDTO.getId() == null) {
            return createInsercion(insercionDTO);
        }
        Insercion insercion = insercionMapper.toEntity(insercionDTO);
        insercion = insercionRepository.save(insercion);
        InsercionDTO result = insercionMapper.toDto(insercion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, insercionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insercions : get all the insercions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of insercions in body
     */
    @GetMapping("/insercions")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<InsercionDTO>> getAllInsercions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Insercions");
        Page<Insercion> page = insercionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/insercions");
        return new ResponseEntity<>(insercionMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /insercions/:id : get the "id" insercion.
     *
     * @param id the id of the insercionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the insercionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/insercions/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<InsercionDTO> getInsercion(@PathVariable Long id) {
        log.debug("REST request to get Insercion : {}", id);
        Insercion insercion = insercionRepository.findOne(id);
        InsercionDTO insercionDTO = insercionMapper.toDto(insercion);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(insercionDTO));
    }

    /**
     * DELETE  /insercions/:id : delete the "id" insercion.
     *
     * @param id the id of the insercionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/insercions/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteInsercion(@PathVariable Long id) {
        log.debug("REST request to delete Insercion : {}", id);
        insercionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
