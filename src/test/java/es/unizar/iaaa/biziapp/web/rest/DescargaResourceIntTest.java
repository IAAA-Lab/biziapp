package es.unizar.iaaa.biziapp.web.rest;

import es.unizar.iaaa.biziapp.JhipsterApp;

import es.unizar.iaaa.biziapp.domain.Descarga;
import es.unizar.iaaa.biziapp.repository.DescargaRepository;
import es.unizar.iaaa.biziapp.service.dto.DescargaDTO;
import es.unizar.iaaa.biziapp.service.mapper.DescargaMapper;
import es.unizar.iaaa.biziapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.unizar.iaaa.biziapp.domain.enumeration.Tipo;
import es.unizar.iaaa.biziapp.domain.enumeration.Estado;
/**
 * Test class for the DescargaResource REST controller.
 *
 * @see DescargaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class DescargaResourceIntTest {

    private static final Tipo DEFAULT_TIPO = Tipo.USOESTACIONES;
    private static final Tipo UPDATED_TIPO = Tipo.MATRIZMOVIMIENTOS;

    private static final String DEFAULT_FECHA_FICHERO = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_FICHERO = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIA = "BBBBBBBBBB";

    private static final String DEFAULT_SUBCATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_SUBCATEGORIA = "BBBBBBBBBB";

    private static final Estado DEFAULT_ESTADO = Estado.WAITING;
    private static final Estado UPDATED_ESTADO = Estado.PROCESING;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DescargaRepository descargaRepository;

    @Autowired
    private DescargaMapper descargaMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDescargaMockMvc;

    private Descarga descarga;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DescargaResource descargaResource = new DescargaResource(descargaRepository, descargaMapper);
        this.restDescargaMockMvc = MockMvcBuilders.standaloneSetup(descargaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Descarga createEntity(EntityManager em) {
        Descarga descarga = new Descarga()
            .tipo(DEFAULT_TIPO)
            .fechaFichero(DEFAULT_FECHA_FICHERO)
            .categoria(DEFAULT_CATEGORIA)
            .subcategoria(DEFAULT_SUBCATEGORIA)
            .estado(DEFAULT_ESTADO)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return descarga;
    }

    @Before
    public void initTest() {
        descarga = createEntity(em);
    }

    @Test
    @Transactional
    public void createDescarga() throws Exception {
        int databaseSizeBeforeCreate = descargaRepository.findAll().size();

        // Create the Descarga
        DescargaDTO descargaDTO = descargaMapper.toDto(descarga);
        restDescargaMockMvc.perform(post("/api/descargas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descargaDTO)))
            .andExpect(status().isCreated());

        // Validate the Descarga in the database
        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeCreate + 1);
        Descarga testDescarga = descargaList.get(descargaList.size() - 1);
        assertThat(testDescarga.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testDescarga.getFechaFichero()).isEqualTo(DEFAULT_FECHA_FICHERO);
        assertThat(testDescarga.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);
        assertThat(testDescarga.getSubcategoria()).isEqualTo(DEFAULT_SUBCATEGORIA);
        assertThat(testDescarga.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testDescarga.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDescarga.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createDescargaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = descargaRepository.findAll().size();

        // Create the Descarga with an existing ID
        descarga.setId(1L);
        DescargaDTO descargaDTO = descargaMapper.toDto(descarga);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescargaMockMvc.perform(post("/api/descargas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descargaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = descargaRepository.findAll().size();
        // set the field null
        descarga.setTipo(null);

        // Create the Descarga, which fails.
        DescargaDTO descargaDTO = descargaMapper.toDto(descarga);

        restDescargaMockMvc.perform(post("/api/descargas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descargaDTO)))
            .andExpect(status().isBadRequest());

        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaFicheroIsRequired() throws Exception {
        int databaseSizeBeforeTest = descargaRepository.findAll().size();
        // set the field null
        descarga.setFechaFichero(null);

        // Create the Descarga, which fails.
        DescargaDTO descargaDTO = descargaMapper.toDto(descarga);

        restDescargaMockMvc.perform(post("/api/descargas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descargaDTO)))
            .andExpect(status().isBadRequest());

        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = descargaRepository.findAll().size();
        // set the field null
        descarga.setCategoria(null);

        // Create the Descarga, which fails.
        DescargaDTO descargaDTO = descargaMapper.toDto(descarga);

        restDescargaMockMvc.perform(post("/api/descargas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descargaDTO)))
            .andExpect(status().isBadRequest());

        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubcategoriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = descargaRepository.findAll().size();
        // set the field null
        descarga.setSubcategoria(null);

        // Create the Descarga, which fails.
        DescargaDTO descargaDTO = descargaMapper.toDto(descarga);

        restDescargaMockMvc.perform(post("/api/descargas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descargaDTO)))
            .andExpect(status().isBadRequest());

        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = descargaRepository.findAll().size();
        // set the field null
        descarga.setEstado(null);

        // Create the Descarga, which fails.
        DescargaDTO descargaDTO = descargaMapper.toDto(descarga);

        restDescargaMockMvc.perform(post("/api/descargas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descargaDTO)))
            .andExpect(status().isBadRequest());

        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDescargas() throws Exception {
        // Initialize the database
        descargaRepository.saveAndFlush(descarga);

        // Get all the descargaList
        restDescargaMockMvc.perform(get("/api/descargas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descarga.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].fechaFichero").value(hasItem(DEFAULT_FECHA_FICHERO.toString())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA.toString())))
            .andExpect(jsonPath("$.[*].subcategoria").value(hasItem(DEFAULT_SUBCATEGORIA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getDescarga() throws Exception {
        // Initialize the database
        descargaRepository.saveAndFlush(descarga);

        // Get the descarga
        restDescargaMockMvc.perform(get("/api/descargas/{id}", descarga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(descarga.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.fechaFichero").value(DEFAULT_FECHA_FICHERO.toString()))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA.toString()))
            .andExpect(jsonPath("$.subcategoria").value(DEFAULT_SUBCATEGORIA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDescarga() throws Exception {
        // Get the descarga
        restDescargaMockMvc.perform(get("/api/descargas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDescarga() throws Exception {
        // Initialize the database
        descargaRepository.saveAndFlush(descarga);
        int databaseSizeBeforeUpdate = descargaRepository.findAll().size();

        // Update the descarga
        Descarga updatedDescarga = descargaRepository.findOne(descarga.getId());
        updatedDescarga
            .tipo(UPDATED_TIPO)
            .fechaFichero(UPDATED_FECHA_FICHERO)
            .categoria(UPDATED_CATEGORIA)
            .subcategoria(UPDATED_SUBCATEGORIA)
            .estado(UPDATED_ESTADO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        DescargaDTO descargaDTO = descargaMapper.toDto(updatedDescarga);

        restDescargaMockMvc.perform(put("/api/descargas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descargaDTO)))
            .andExpect(status().isOk());

        // Validate the Descarga in the database
        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeUpdate);
        Descarga testDescarga = descargaList.get(descargaList.size() - 1);
        assertThat(testDescarga.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testDescarga.getFechaFichero()).isEqualTo(UPDATED_FECHA_FICHERO);
        assertThat(testDescarga.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testDescarga.getSubcategoria()).isEqualTo(UPDATED_SUBCATEGORIA);
        assertThat(testDescarga.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testDescarga.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDescarga.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingDescarga() throws Exception {
        int databaseSizeBeforeUpdate = descargaRepository.findAll().size();

        // Create the Descarga
        DescargaDTO descargaDTO = descargaMapper.toDto(descarga);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDescargaMockMvc.perform(put("/api/descargas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descargaDTO)))
            .andExpect(status().isCreated());

        // Validate the Descarga in the database
        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDescarga() throws Exception {
        // Initialize the database
        descargaRepository.saveAndFlush(descarga);
        int databaseSizeBeforeDelete = descargaRepository.findAll().size();

        // Get the descarga
        restDescargaMockMvc.perform(delete("/api/descargas/{id}", descarga.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Descarga> descargaList = descargaRepository.findAll();
        assertThat(descargaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Descarga.class);
        Descarga descarga1 = new Descarga();
        descarga1.setId(1L);
        Descarga descarga2 = new Descarga();
        descarga2.setId(descarga1.getId());
        assertThat(descarga1).isEqualTo(descarga2);
        descarga2.setId(2L);
        assertThat(descarga1).isNotEqualTo(descarga2);
        descarga1.setId(null);
        assertThat(descarga1).isNotEqualTo(descarga2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescargaDTO.class);
        DescargaDTO descargaDTO1 = new DescargaDTO();
        descargaDTO1.setId(1L);
        DescargaDTO descargaDTO2 = new DescargaDTO();
        assertThat(descargaDTO1).isNotEqualTo(descargaDTO2);
        descargaDTO2.setId(descargaDTO1.getId());
        assertThat(descargaDTO1).isEqualTo(descargaDTO2);
        descargaDTO2.setId(2L);
        assertThat(descargaDTO1).isNotEqualTo(descargaDTO2);
        descargaDTO1.setId(null);
        assertThat(descargaDTO1).isNotEqualTo(descargaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(descargaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(descargaMapper.fromId(null)).isNull();
    }
}
