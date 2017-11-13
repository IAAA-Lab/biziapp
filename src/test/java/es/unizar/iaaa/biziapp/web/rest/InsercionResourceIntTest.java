package es.unizar.iaaa.biziapp.web.rest;

import es.unizar.iaaa.biziapp.JhipsterApp;

import es.unizar.iaaa.biziapp.domain.Insercion;
import es.unizar.iaaa.biziapp.repository.InsercionRepository;
import es.unizar.iaaa.biziapp.service.dto.InsercionDTO;
import es.unizar.iaaa.biziapp.service.mapper.InsercionMapper;
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
 * Test class for the InsercionResource REST controller.
 *
 * @see InsercionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class InsercionResourceIntTest {

    private static final Long DEFAULT_ID_TAREA = 1L;
    private static final Long UPDATED_ID_TAREA = 2L;

    private static final Tipo DEFAULT_TIPO = Tipo.USOESTACIONES;
    private static final Tipo UPDATED_TIPO = Tipo.MATRIZMOVIMIENTOS;

    private static final String DEFAULT_FECHA_FICHERO = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_FICHERO = "BBBBBBBBBB";

    private static final String DEFAULT_PATH_FICHERO_CSV = "AAAAAAAAAA";
    private static final String UPDATED_PATH_FICHERO_CSV = "BBBBBBBBBB";

    private static final Estado DEFAULT_ESTADO = Estado.WAITING;
    private static final Estado UPDATED_ESTADO = Estado.PROCESING;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private InsercionRepository insercionRepository;

    @Autowired
    private InsercionMapper insercionMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInsercionMockMvc;

    private Insercion insercion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InsercionResource insercionResource = new InsercionResource(insercionRepository, insercionMapper);
        this.restInsercionMockMvc = MockMvcBuilders.standaloneSetup(insercionResource)
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
    public static Insercion createEntity(EntityManager em) {
        Insercion insercion = new Insercion()
            .idTarea(DEFAULT_ID_TAREA)
            .tipo(DEFAULT_TIPO)
            .fechaFichero(DEFAULT_FECHA_FICHERO)
            .pathFicheroCSV(DEFAULT_PATH_FICHERO_CSV)
            .estado(DEFAULT_ESTADO)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return insercion;
    }

    @Before
    public void initTest() {
        insercion = createEntity(em);
    }

    @Test
    @Transactional
    public void createInsercion() throws Exception {
        int databaseSizeBeforeCreate = insercionRepository.findAll().size();

        // Create the Insercion
        InsercionDTO insercionDTO = insercionMapper.toDto(insercion);
        restInsercionMockMvc.perform(post("/api/insercions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insercionDTO)))
            .andExpect(status().isCreated());

        // Validate the Insercion in the database
        List<Insercion> insercionList = insercionRepository.findAll();
        assertThat(insercionList).hasSize(databaseSizeBeforeCreate + 1);
        Insercion testInsercion = insercionList.get(insercionList.size() - 1);
        assertThat(testInsercion.getIdTarea()).isEqualTo(DEFAULT_ID_TAREA);
        assertThat(testInsercion.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testInsercion.getFechaFichero()).isEqualTo(DEFAULT_FECHA_FICHERO);
        assertThat(testInsercion.getPathFicheroCSV()).isEqualTo(DEFAULT_PATH_FICHERO_CSV);
        assertThat(testInsercion.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testInsercion.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testInsercion.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createInsercionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = insercionRepository.findAll().size();

        // Create the Insercion with an existing ID
        insercion.setId(1L);
        InsercionDTO insercionDTO = insercionMapper.toDto(insercion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsercionMockMvc.perform(post("/api/insercions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insercionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Insercion> insercionList = insercionRepository.findAll();
        assertThat(insercionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = insercionRepository.findAll().size();
        // set the field null
        insercion.setTipo(null);

        // Create the Insercion, which fails.
        InsercionDTO insercionDTO = insercionMapper.toDto(insercion);

        restInsercionMockMvc.perform(post("/api/insercions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insercionDTO)))
            .andExpect(status().isBadRequest());

        List<Insercion> insercionList = insercionRepository.findAll();
        assertThat(insercionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaFicheroIsRequired() throws Exception {
        int databaseSizeBeforeTest = insercionRepository.findAll().size();
        // set the field null
        insercion.setFechaFichero(null);

        // Create the Insercion, which fails.
        InsercionDTO insercionDTO = insercionMapper.toDto(insercion);

        restInsercionMockMvc.perform(post("/api/insercions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insercionDTO)))
            .andExpect(status().isBadRequest());

        List<Insercion> insercionList = insercionRepository.findAll();
        assertThat(insercionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPathFicheroCSVIsRequired() throws Exception {
        int databaseSizeBeforeTest = insercionRepository.findAll().size();
        // set the field null
        insercion.setPathFicheroCSV(null);

        // Create the Insercion, which fails.
        InsercionDTO insercionDTO = insercionMapper.toDto(insercion);

        restInsercionMockMvc.perform(post("/api/insercions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insercionDTO)))
            .andExpect(status().isBadRequest());

        List<Insercion> insercionList = insercionRepository.findAll();
        assertThat(insercionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = insercionRepository.findAll().size();
        // set the field null
        insercion.setEstado(null);

        // Create the Insercion, which fails.
        InsercionDTO insercionDTO = insercionMapper.toDto(insercion);

        restInsercionMockMvc.perform(post("/api/insercions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insercionDTO)))
            .andExpect(status().isBadRequest());

        List<Insercion> insercionList = insercionRepository.findAll();
        assertThat(insercionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInsercions() throws Exception {
        // Initialize the database
        insercionRepository.saveAndFlush(insercion);

        // Get all the insercionList
        restInsercionMockMvc.perform(get("/api/insercions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insercion.getId().intValue())))
            .andExpect(jsonPath("$.[*].idTarea").value(hasItem(DEFAULT_ID_TAREA.intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].fechaFichero").value(hasItem(DEFAULT_FECHA_FICHERO.toString())))
            .andExpect(jsonPath("$.[*].pathFicheroCSV").value(hasItem(DEFAULT_PATH_FICHERO_CSV.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getInsercion() throws Exception {
        // Initialize the database
        insercionRepository.saveAndFlush(insercion);

        // Get the insercion
        restInsercionMockMvc.perform(get("/api/insercions/{id}", insercion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(insercion.getId().intValue()))
            .andExpect(jsonPath("$.idTarea").value(DEFAULT_ID_TAREA.intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.fechaFichero").value(DEFAULT_FECHA_FICHERO.toString()))
            .andExpect(jsonPath("$.pathFicheroCSV").value(DEFAULT_PATH_FICHERO_CSV.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInsercion() throws Exception {
        // Get the insercion
        restInsercionMockMvc.perform(get("/api/insercions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInsercion() throws Exception {
        // Initialize the database
        insercionRepository.saveAndFlush(insercion);
        int databaseSizeBeforeUpdate = insercionRepository.findAll().size();

        // Update the insercion
        Insercion updatedInsercion = insercionRepository.findOne(insercion.getId());
        updatedInsercion
            .idTarea(UPDATED_ID_TAREA)
            .tipo(UPDATED_TIPO)
            .fechaFichero(UPDATED_FECHA_FICHERO)
            .pathFicheroCSV(UPDATED_PATH_FICHERO_CSV)
            .estado(UPDATED_ESTADO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        InsercionDTO insercionDTO = insercionMapper.toDto(updatedInsercion);

        restInsercionMockMvc.perform(put("/api/insercions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insercionDTO)))
            .andExpect(status().isOk());

        // Validate the Insercion in the database
        List<Insercion> insercionList = insercionRepository.findAll();
        assertThat(insercionList).hasSize(databaseSizeBeforeUpdate);
        Insercion testInsercion = insercionList.get(insercionList.size() - 1);
        assertThat(testInsercion.getIdTarea()).isEqualTo(UPDATED_ID_TAREA);
        assertThat(testInsercion.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testInsercion.getFechaFichero()).isEqualTo(UPDATED_FECHA_FICHERO);
        assertThat(testInsercion.getPathFicheroCSV()).isEqualTo(UPDATED_PATH_FICHERO_CSV);
        assertThat(testInsercion.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testInsercion.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInsercion.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingInsercion() throws Exception {
        int databaseSizeBeforeUpdate = insercionRepository.findAll().size();

        // Create the Insercion
        InsercionDTO insercionDTO = insercionMapper.toDto(insercion);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInsercionMockMvc.perform(put("/api/insercions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(insercionDTO)))
            .andExpect(status().isCreated());

        // Validate the Insercion in the database
        List<Insercion> insercionList = insercionRepository.findAll();
        assertThat(insercionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInsercion() throws Exception {
        // Initialize the database
        insercionRepository.saveAndFlush(insercion);
        int databaseSizeBeforeDelete = insercionRepository.findAll().size();

        // Get the insercion
        restInsercionMockMvc.perform(delete("/api/insercions/{id}", insercion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Insercion> insercionList = insercionRepository.findAll();
        assertThat(insercionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Insercion.class);
        Insercion insercion1 = new Insercion();
        insercion1.setId(1L);
        Insercion insercion2 = new Insercion();
        insercion2.setId(insercion1.getId());
        assertThat(insercion1).isEqualTo(insercion2);
        insercion2.setId(2L);
        assertThat(insercion1).isNotEqualTo(insercion2);
        insercion1.setId(null);
        assertThat(insercion1).isNotEqualTo(insercion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsercionDTO.class);
        InsercionDTO insercionDTO1 = new InsercionDTO();
        insercionDTO1.setId(1L);
        InsercionDTO insercionDTO2 = new InsercionDTO();
        assertThat(insercionDTO1).isNotEqualTo(insercionDTO2);
        insercionDTO2.setId(insercionDTO1.getId());
        assertThat(insercionDTO1).isEqualTo(insercionDTO2);
        insercionDTO2.setId(2L);
        assertThat(insercionDTO1).isNotEqualTo(insercionDTO2);
        insercionDTO1.setId(null);
        assertThat(insercionDTO1).isNotEqualTo(insercionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(insercionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(insercionMapper.fromId(null)).isNull();
    }
}
