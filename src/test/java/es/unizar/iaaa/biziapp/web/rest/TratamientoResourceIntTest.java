package es.unizar.iaaa.biziapp.web.rest;

import es.unizar.iaaa.biziapp.JhipsterApp;

import es.unizar.iaaa.biziapp.domain.Tratamiento;
import es.unizar.iaaa.biziapp.repository.TratamientoRepository;
import es.unizar.iaaa.biziapp.service.dto.TratamientoDTO;
import es.unizar.iaaa.biziapp.service.mapper.TratamientoMapper;
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
 * Test class for the TratamientoResource REST controller.
 *
 * @see TratamientoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class TratamientoResourceIntTest {

    private static final Long DEFAULT_ID_TAREA = 1L;
    private static final Long UPDATED_ID_TAREA = 2L;

    private static final Tipo DEFAULT_TIPO = Tipo.USOESTACIONES;
    private static final Tipo UPDATED_TIPO = Tipo.MATRIZMOVIMIENTOS;

    private static final String DEFAULT_FECHA_FICHERO = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_FICHERO = "BBBBBBBBBB";

    private static final String DEFAULT_PATH_FICHERO_XLS = "AAAAAAAAAA";
    private static final String UPDATED_PATH_FICHERO_XLS = "BBBBBBBBBB";

    private static final Estado DEFAULT_ESTADO = Estado.WAITING;
    private static final Estado UPDATED_ESTADO = Estado.PROCESING;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private TratamientoMapper tratamientoMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTratamientoMockMvc;

    private Tratamiento tratamiento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TratamientoResource tratamientoResource = new TratamientoResource(tratamientoRepository, tratamientoMapper);
        this.restTratamientoMockMvc = MockMvcBuilders.standaloneSetup(tratamientoResource)
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
    public static Tratamiento createEntity(EntityManager em) {
        Tratamiento tratamiento = new Tratamiento()
            .idTarea(DEFAULT_ID_TAREA)
            .tipo(DEFAULT_TIPO)
            .fechaFichero(DEFAULT_FECHA_FICHERO)
            .pathFicheroXLS(DEFAULT_PATH_FICHERO_XLS)
            .estado(DEFAULT_ESTADO)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return tratamiento;
    }

    @Before
    public void initTest() {
        tratamiento = createEntity(em);
    }

    @Test
    @Transactional
    public void createTratamiento() throws Exception {
        int databaseSizeBeforeCreate = tratamientoRepository.findAll().size();

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);
        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamientoDTO)))
            .andExpect(status().isCreated());

        // Validate the Tratamiento in the database
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeCreate + 1);
        Tratamiento testTratamiento = tratamientoList.get(tratamientoList.size() - 1);
        assertThat(testTratamiento.getIdTarea()).isEqualTo(DEFAULT_ID_TAREA);
        assertThat(testTratamiento.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testTratamiento.getFechaFichero()).isEqualTo(DEFAULT_FECHA_FICHERO);
        assertThat(testTratamiento.getPathFicheroXLS()).isEqualTo(DEFAULT_PATH_FICHERO_XLS);
        assertThat(testTratamiento.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testTratamiento.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTratamiento.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createTratamientoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tratamientoRepository.findAll().size();

        // Create the Tratamiento with an existing ID
        tratamiento.setId(1L);
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tratamientoRepository.findAll().size();
        // set the field null
        tratamiento.setTipo(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaFicheroIsRequired() throws Exception {
        int databaseSizeBeforeTest = tratamientoRepository.findAll().size();
        // set the field null
        tratamiento.setFechaFichero(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPathFicheroXLSIsRequired() throws Exception {
        int databaseSizeBeforeTest = tratamientoRepository.findAll().size();
        // set the field null
        tratamiento.setPathFicheroXLS(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tratamientoRepository.findAll().size();
        // set the field null
        tratamiento.setEstado(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTratamientos() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList
        restTratamientoMockMvc.perform(get("/api/tratamientos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tratamiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].idTarea").value(hasItem(DEFAULT_ID_TAREA.intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].fechaFichero").value(hasItem(DEFAULT_FECHA_FICHERO.toString())))
            .andExpect(jsonPath("$.[*].pathFicheroXLS").value(hasItem(DEFAULT_PATH_FICHERO_XLS.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        // Get the tratamiento
        restTratamientoMockMvc.perform(get("/api/tratamientos/{id}", tratamiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tratamiento.getId().intValue()))
            .andExpect(jsonPath("$.idTarea").value(DEFAULT_ID_TAREA.intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.fechaFichero").value(DEFAULT_FECHA_FICHERO.toString()))
            .andExpect(jsonPath("$.pathFicheroXLS").value(DEFAULT_PATH_FICHERO_XLS.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTratamiento() throws Exception {
        // Get the tratamiento
        restTratamientoMockMvc.perform(get("/api/tratamientos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);
        int databaseSizeBeforeUpdate = tratamientoRepository.findAll().size();

        // Update the tratamiento
        Tratamiento updatedTratamiento = tratamientoRepository.findOne(tratamiento.getId());
        updatedTratamiento
            .idTarea(UPDATED_ID_TAREA)
            .tipo(UPDATED_TIPO)
            .fechaFichero(UPDATED_FECHA_FICHERO)
            .pathFicheroXLS(UPDATED_PATH_FICHERO_XLS)
            .estado(UPDATED_ESTADO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(updatedTratamiento);

        restTratamientoMockMvc.perform(put("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamientoDTO)))
            .andExpect(status().isOk());

        // Validate the Tratamiento in the database
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeUpdate);
        Tratamiento testTratamiento = tratamientoList.get(tratamientoList.size() - 1);
        assertThat(testTratamiento.getIdTarea()).isEqualTo(UPDATED_ID_TAREA);
        assertThat(testTratamiento.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTratamiento.getFechaFichero()).isEqualTo(UPDATED_FECHA_FICHERO);
        assertThat(testTratamiento.getPathFicheroXLS()).isEqualTo(UPDATED_PATH_FICHERO_XLS);
        assertThat(testTratamiento.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testTratamiento.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTratamiento.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingTratamiento() throws Exception {
        int databaseSizeBeforeUpdate = tratamientoRepository.findAll().size();

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTratamientoMockMvc.perform(put("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamientoDTO)))
            .andExpect(status().isCreated());

        // Validate the Tratamiento in the database
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);
        int databaseSizeBeforeDelete = tratamientoRepository.findAll().size();

        // Get the tratamiento
        restTratamientoMockMvc.perform(delete("/api/tratamientos/{id}", tratamiento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tratamiento.class);
        Tratamiento tratamiento1 = new Tratamiento();
        tratamiento1.setId(1L);
        Tratamiento tratamiento2 = new Tratamiento();
        tratamiento2.setId(tratamiento1.getId());
        assertThat(tratamiento1).isEqualTo(tratamiento2);
        tratamiento2.setId(2L);
        assertThat(tratamiento1).isNotEqualTo(tratamiento2);
        tratamiento1.setId(null);
        assertThat(tratamiento1).isNotEqualTo(tratamiento2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TratamientoDTO.class);
        TratamientoDTO tratamientoDTO1 = new TratamientoDTO();
        tratamientoDTO1.setId(1L);
        TratamientoDTO tratamientoDTO2 = new TratamientoDTO();
        assertThat(tratamientoDTO1).isNotEqualTo(tratamientoDTO2);
        tratamientoDTO2.setId(tratamientoDTO1.getId());
        assertThat(tratamientoDTO1).isEqualTo(tratamientoDTO2);
        tratamientoDTO2.setId(2L);
        assertThat(tratamientoDTO1).isNotEqualTo(tratamientoDTO2);
        tratamientoDTO1.setId(null);
        assertThat(tratamientoDTO1).isNotEqualTo(tratamientoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tratamientoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tratamientoMapper.fromId(null)).isNull();
    }
}
