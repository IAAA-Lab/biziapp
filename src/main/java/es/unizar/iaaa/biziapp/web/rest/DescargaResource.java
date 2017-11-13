package es.unizar.iaaa.biziapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.biziapp.domain.Descarga;

import es.unizar.iaaa.biziapp.repository.DescargaRepository;
import es.unizar.iaaa.biziapp.security.AuthoritiesConstants;
import es.unizar.iaaa.biziapp.web.rest.util.HeaderUtil;
import es.unizar.iaaa.biziapp.web.rest.util.PaginationUtil;
import es.unizar.iaaa.biziapp.service.dto.DescargaDTO;
import es.unizar.iaaa.biziapp.service.mapper.DescargaMapper;
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
 * REST controller for managing Descarga.
 */
@RestController
@RequestMapping("/api")
public class DescargaResource {

    private final Logger log = LoggerFactory.getLogger(DescargaResource.class);

    private static final String ENTITY_NAME = "descarga";

    private final DescargaRepository descargaRepository;

    private final DescargaMapper descargaMapper;

    public DescargaResource(DescargaRepository descargaRepository, DescargaMapper descargaMapper) {
        this.descargaRepository = descargaRepository;
        this.descargaMapper = descargaMapper;
    }

    /**
     * POST  /descargas : Create a new descarga.
     *
     * @param descargaDTO the descargaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new descargaDTO, or with status 400 (Bad Request) if the descarga has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/descargas")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<DescargaDTO> createDescarga(@Valid @RequestBody DescargaDTO descargaDTO) throws URISyntaxException {
        log.debug("REST request to save Descarga : {}", descargaDTO);
        if (descargaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new descarga cannot already have an ID")).body(null);
        }
        Descarga descarga = descargaMapper.toEntity(descargaDTO);
        descarga = descargaRepository.save(descarga);
        DescargaDTO result = descargaMapper.toDto(descarga);
        return ResponseEntity.created(new URI("/api/descargas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /descargas : Updates an existing descarga.
     *
     * @param descargaDTO the descargaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated descargaDTO,
     * or with status 400 (Bad Request) if the descargaDTO is not valid,
     * or with status 500 (Internal Server Error) if the descargaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/descargas")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<DescargaDTO> updateDescarga(@Valid @RequestBody DescargaDTO descargaDTO) throws URISyntaxException {
        log.debug("REST request to update Descarga : {}", descargaDTO);
        if (descargaDTO.getId() == null) {
            return createDescarga(descargaDTO);
        }
        Descarga descarga = descargaMapper.toEntity(descargaDTO);
        descarga = descargaRepository.save(descarga);
        DescargaDTO result = descargaMapper.toDto(descarga);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, descargaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /descargas : get all the descargas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of descargas in body
     */
    @GetMapping("/descargas")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<DescargaDTO>> getAllDescargas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Descargas");
        Page<Descarga> page = descargaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/descargas");
        return new ResponseEntity<>(descargaMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /descargas/:id : get the "id" descarga.
     *
     * @param id the id of the descargaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the descargaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/descargas/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<DescargaDTO> getDescarga(@PathVariable Long id) {
        log.debug("REST request to get Descarga : {}", id);
        Descarga descarga = descargaRepository.findOne(id);
        DescargaDTO descargaDTO = descargaMapper.toDto(descarga);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(descargaDTO));
    }

    /**
     * DELETE  /descargas/:id : delete the "id" descarga.
     *
     * @param id the id of the descargaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/descargas/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteDescarga(@PathVariable Long id) {
        log.debug("REST request to delete Descarga : {}", id);
        descargaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
