package es.unizar.iaaa.biziapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.biziapp.domain.Usoestacion;

import es.unizar.iaaa.biziapp.repository.UsoestacionRepository;
import es.unizar.iaaa.biziapp.web.rest.util.HeaderUtil;
import es.unizar.iaaa.biziapp.web.rest.util.PaginationUtil;
import es.unizar.iaaa.biziapp.service.dto.UsoestacionDTO;
import es.unizar.iaaa.biziapp.service.mapper.UsoestacionMapper;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Usoestacion.
 */
@RestController
@RequestMapping("/api")
public class UsoestacionResource {

    private final Logger log = LoggerFactory.getLogger(UsoestacionResource.class);

    private static final String ENTITY_NAME = "usoestacion";

    private final UsoestacionRepository usoestacionRepository;

    private final UsoestacionMapper usoestacionMapper;

    public UsoestacionResource(UsoestacionRepository usoestacionRepository, UsoestacionMapper usoestacionMapper) {
        this.usoestacionRepository = usoestacionRepository;
        this.usoestacionMapper = usoestacionMapper;
    }

    /**
     * POST  /usoestacions : Create a new usoestacion.
     *
     * @param usoestacionDTO the usoestacionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new usoestacionDTO, or with status 400 (Bad Request) if the usoestacion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/usoestacions")
    @Timed
    public ResponseEntity<UsoestacionDTO> createUsoestacion(@RequestBody UsoestacionDTO usoestacionDTO) throws URISyntaxException {
        log.debug("REST request to save Usoestacion : {}", usoestacionDTO);
        if (usoestacionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new usoestacion cannot already have an ID")).body(null);
        }
        Usoestacion usoestacion = usoestacionMapper.toEntity(usoestacionDTO);
        usoestacion = usoestacionRepository.save(usoestacion);
        UsoestacionDTO result = usoestacionMapper.toDto(usoestacion);
        return ResponseEntity.created(new URI("/api/usoestacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /usoestacions : Updates an existing usoestacion.
     *
     * @param usoestacionDTO the usoestacionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated usoestacionDTO,
     * or with status 400 (Bad Request) if the usoestacionDTO is not valid,
     * or with status 500 (Internal Server Error) if the usoestacionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/usoestacions")
    @Timed
    public ResponseEntity<UsoestacionDTO> updateUsoestacion(@RequestBody UsoestacionDTO usoestacionDTO) throws URISyntaxException {
        log.debug("REST request to update Usoestacion : {}", usoestacionDTO);
        if (usoestacionDTO.getId() == null) {
            return createUsoestacion(usoestacionDTO);
        }
        Usoestacion usoestacion = usoestacionMapper.toEntity(usoestacionDTO);
        usoestacion = usoestacionRepository.save(usoestacion);
        UsoestacionDTO result = usoestacionMapper.toDto(usoestacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, usoestacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /usoestacions : get all the usoestacions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of usoestacions in body
     */
    @GetMapping("/usoestacions")
    @Timed
    public ResponseEntity<List<UsoestacionDTO>> getAllUsoestacions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Usoestacions");
        Page<Usoestacion> page = usoestacionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/usoestacions");
        return new ResponseEntity<>(usoestacionMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /usoestacions/:id : get the "id" usoestacion.
     *
     * @param id the id of the usoestacionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the usoestacionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/usoestacions/{id}")
    @Timed
    public ResponseEntity<UsoestacionDTO> getUsoestacion(@PathVariable Long id) {
        log.debug("REST request to get Usoestacion : {}", id);
        Usoestacion usoestacion = usoestacionRepository.findOne(id);
        UsoestacionDTO usoestacionDTO = usoestacionMapper.toDto(usoestacion);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(usoestacionDTO));
    }

    /**
     * DELETE  /usoestacions/:id : delete the "id" usoestacion.
     *
     * @param id the id of the usoestacionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/usoestacions/{id}")
    @Timed
    public ResponseEntity<Void> deleteUsoestacion(@PathVariable Long id) {
        log.debug("REST request to delete Usoestacion : {}", id);
        usoestacionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
