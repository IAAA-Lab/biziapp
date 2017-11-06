package es.unizar.iaaa.biziapp.web.rest;

import es.unizar.iaaa.biziapp.JhipsterApp;

import es.unizar.iaaa.biziapp.domain.Usoestacion;
import es.unizar.iaaa.biziapp.repository.UsoestacionRepository;
import es.unizar.iaaa.biziapp.service.dto.UsoestacionDTO;
import es.unizar.iaaa.biziapp.service.mapper.UsoestacionMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UsoestacionResource REST controller.
 *
 * @see UsoestacionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class UsoestacionResourceIntTest {

    private static final String DEFAULT_NOMBRE_COMPLETO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_COMPLETO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_ESTACION = 1;
    private static final Integer UPDATED_ID_ESTACION = 2;

    private static final String DEFAULT_NOMBRE_ESTACION = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_ESTACION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_DE_USO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_DE_USO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_INTERVALO_DE_TIEMPO = "AAAAAAAAAA";
    private static final String UPDATED_INTERVALO_DE_TIEMPO = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEVOLUCION_TOTAL = 1;
    private static final Integer UPDATED_DEVOLUCION_TOTAL = 2;

    private static final Float DEFAULT_DEVOLUCION_MEDIA = 1F;
    private static final Float UPDATED_DEVOLUCION_MEDIA = 2F;

    private static final Integer DEFAULT_RETIRADAS_TOTAL = 1;
    private static final Integer UPDATED_RETIRADAS_TOTAL = 2;

    private static final Float DEFAULT_RETIRADAS_MEDIA = 1F;
    private static final Float UPDATED_RETIRADAS_MEDIA = 2F;

    private static final Float DEFAULT_NETO = 1F;
    private static final Float UPDATED_NETO = 2F;

    private static final Float DEFAULT_TOTAL = 1F;
    private static final Float UPDATED_TOTAL = 2F;

    private static final LocalDate DEFAULT_FECHA_OBTENCION_DATOS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_OBTENCION_DATOS = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FICHERO_CSV = "AAAAAAAAAA";
    private static final String UPDATED_FICHERO_CSV = "BBBBBBBBBB";

    private static final String DEFAULT_FICHERO_XLS = "AAAAAAAAAA";
    private static final String UPDATED_FICHERO_XLS = "BBBBBBBBBB";

    private static final String DEFAULT_HASHCODE = "AAAAAAAAAA";
    private static final String UPDATED_HASHCODE = "BBBBBBBBBB";

    @Autowired
    private UsoestacionRepository usoestacionRepository;

    @Autowired
    private UsoestacionMapper usoestacionMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUsoestacionMockMvc;

    private Usoestacion usoestacion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UsoestacionResource usoestacionResource = new UsoestacionResource(usoestacionRepository, usoestacionMapper);
        this.restUsoestacionMockMvc = MockMvcBuilders.standaloneSetup(usoestacionResource)
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
    public static Usoestacion createEntity(EntityManager em) {
        Usoestacion usoestacion = new Usoestacion()
            .nombreCompleto(DEFAULT_NOMBRE_COMPLETO)
            .idEstacion(DEFAULT_ID_ESTACION)
            .nombreEstacion(DEFAULT_NOMBRE_ESTACION)
            .fechaDeUso(DEFAULT_FECHA_DE_USO)
            .intervaloDeTiempo(DEFAULT_INTERVALO_DE_TIEMPO)
            .devolucionTotal(DEFAULT_DEVOLUCION_TOTAL)
            .devolucionMedia(DEFAULT_DEVOLUCION_MEDIA)
            .retiradasTotal(DEFAULT_RETIRADAS_TOTAL)
            .retiradasMedia(DEFAULT_RETIRADAS_MEDIA)
            .neto(DEFAULT_NETO)
            .total(DEFAULT_TOTAL)
            .fechaObtencionDatos(DEFAULT_FECHA_OBTENCION_DATOS)
            .ficheroCSV(DEFAULT_FICHERO_CSV)
            .ficheroXLS(DEFAULT_FICHERO_XLS)
            .hashcode(DEFAULT_HASHCODE);
        return usoestacion;
    }

    @Before
    public void initTest() {
        usoestacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createUsoestacion() throws Exception {
        int databaseSizeBeforeCreate = usoestacionRepository.findAll().size();

        // Create the Usoestacion
        UsoestacionDTO usoestacionDTO = usoestacionMapper.toDto(usoestacion);
        restUsoestacionMockMvc.perform(post("/api/usoestacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usoestacionDTO)))
            .andExpect(status().isCreated());

        // Validate the Usoestacion in the database
        List<Usoestacion> usoestacionList = usoestacionRepository.findAll();
        assertThat(usoestacionList).hasSize(databaseSizeBeforeCreate + 1);
        Usoestacion testUsoestacion = usoestacionList.get(usoestacionList.size() - 1);
        assertThat(testUsoestacion.getNombreCompleto()).isEqualTo(DEFAULT_NOMBRE_COMPLETO);
        assertThat(testUsoestacion.getIdEstacion()).isEqualTo(DEFAULT_ID_ESTACION);
        assertThat(testUsoestacion.getNombreEstacion()).isEqualTo(DEFAULT_NOMBRE_ESTACION);
        assertThat(testUsoestacion.getFechaDeUso()).isEqualTo(DEFAULT_FECHA_DE_USO);
        assertThat(testUsoestacion.getIntervaloDeTiempo()).isEqualTo(DEFAULT_INTERVALO_DE_TIEMPO);
        assertThat(testUsoestacion.getDevolucionTotal()).isEqualTo(DEFAULT_DEVOLUCION_TOTAL);
        assertThat(testUsoestacion.getDevolucionMedia()).isEqualTo(DEFAULT_DEVOLUCION_MEDIA);
        assertThat(testUsoestacion.getRetiradasTotal()).isEqualTo(DEFAULT_RETIRADAS_TOTAL);
        assertThat(testUsoestacion.getRetiradasMedia()).isEqualTo(DEFAULT_RETIRADAS_MEDIA);
        assertThat(testUsoestacion.getNeto()).isEqualTo(DEFAULT_NETO);
        assertThat(testUsoestacion.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testUsoestacion.getFechaObtencionDatos()).isEqualTo(DEFAULT_FECHA_OBTENCION_DATOS);
        assertThat(testUsoestacion.getFicheroCSV()).isEqualTo(DEFAULT_FICHERO_CSV);
        assertThat(testUsoestacion.getFicheroXLS()).isEqualTo(DEFAULT_FICHERO_XLS);
        assertThat(testUsoestacion.getHashcode()).isEqualTo(DEFAULT_HASHCODE);
    }

    @Test
    @Transactional
    public void createUsoestacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = usoestacionRepository.findAll().size();

        // Create the Usoestacion with an existing ID
        usoestacion.setId(1L);
        UsoestacionDTO usoestacionDTO = usoestacionMapper.toDto(usoestacion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsoestacionMockMvc.perform(post("/api/usoestacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usoestacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Usoestacion> usoestacionList = usoestacionRepository.findAll();
        assertThat(usoestacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUsoestacions() throws Exception {
        // Initialize the database
        usoestacionRepository.saveAndFlush(usoestacion);

        // Get all the usoestacionList
        restUsoestacionMockMvc.perform(get("/api/usoestacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usoestacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreCompleto").value(hasItem(DEFAULT_NOMBRE_COMPLETO.toString())))
            .andExpect(jsonPath("$.[*].idEstacion").value(hasItem(DEFAULT_ID_ESTACION)))
            .andExpect(jsonPath("$.[*].nombreEstacion").value(hasItem(DEFAULT_NOMBRE_ESTACION.toString())))
            .andExpect(jsonPath("$.[*].fechaDeUso").value(hasItem(DEFAULT_FECHA_DE_USO.toString())))
            .andExpect(jsonPath("$.[*].intervaloDeTiempo").value(hasItem(DEFAULT_INTERVALO_DE_TIEMPO.toString())))
            .andExpect(jsonPath("$.[*].devolucionTotal").value(hasItem(DEFAULT_DEVOLUCION_TOTAL)))
            .andExpect(jsonPath("$.[*].devolucionMedia").value(hasItem(DEFAULT_DEVOLUCION_MEDIA.doubleValue())))
            .andExpect(jsonPath("$.[*].retiradasTotal").value(hasItem(DEFAULT_RETIRADAS_TOTAL)))
            .andExpect(jsonPath("$.[*].retiradasMedia").value(hasItem(DEFAULT_RETIRADAS_MEDIA.doubleValue())))
            .andExpect(jsonPath("$.[*].neto").value(hasItem(DEFAULT_NETO.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaObtencionDatos").value(hasItem(DEFAULT_FECHA_OBTENCION_DATOS.toString())))
            .andExpect(jsonPath("$.[*].ficheroCSV").value(hasItem(DEFAULT_FICHERO_CSV.toString())))
            .andExpect(jsonPath("$.[*].ficheroXLS").value(hasItem(DEFAULT_FICHERO_XLS.toString())))
            .andExpect(jsonPath("$.[*].hashcode").value(hasItem(DEFAULT_HASHCODE.toString())));
    }

    @Test
    @Transactional
    public void getUsoestacion() throws Exception {
        // Initialize the database
        usoestacionRepository.saveAndFlush(usoestacion);

        // Get the usoestacion
        restUsoestacionMockMvc.perform(get("/api/usoestacions/{id}", usoestacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(usoestacion.getId().intValue()))
            .andExpect(jsonPath("$.nombreCompleto").value(DEFAULT_NOMBRE_COMPLETO.toString()))
            .andExpect(jsonPath("$.idEstacion").value(DEFAULT_ID_ESTACION))
            .andExpect(jsonPath("$.nombreEstacion").value(DEFAULT_NOMBRE_ESTACION.toString()))
            .andExpect(jsonPath("$.fechaDeUso").value(DEFAULT_FECHA_DE_USO.toString()))
            .andExpect(jsonPath("$.intervaloDeTiempo").value(DEFAULT_INTERVALO_DE_TIEMPO.toString()))
            .andExpect(jsonPath("$.devolucionTotal").value(DEFAULT_DEVOLUCION_TOTAL))
            .andExpect(jsonPath("$.devolucionMedia").value(DEFAULT_DEVOLUCION_MEDIA.doubleValue()))
            .andExpect(jsonPath("$.retiradasTotal").value(DEFAULT_RETIRADAS_TOTAL))
            .andExpect(jsonPath("$.retiradasMedia").value(DEFAULT_RETIRADAS_MEDIA.doubleValue()))
            .andExpect(jsonPath("$.neto").value(DEFAULT_NETO.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.fechaObtencionDatos").value(DEFAULT_FECHA_OBTENCION_DATOS.toString()))
            .andExpect(jsonPath("$.ficheroCSV").value(DEFAULT_FICHERO_CSV.toString()))
            .andExpect(jsonPath("$.ficheroXLS").value(DEFAULT_FICHERO_XLS.toString()))
            .andExpect(jsonPath("$.hashcode").value(DEFAULT_HASHCODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUsoestacion() throws Exception {
        // Get the usoestacion
        restUsoestacionMockMvc.perform(get("/api/usoestacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsoestacion() throws Exception {
        // Initialize the database
        usoestacionRepository.saveAndFlush(usoestacion);
        int databaseSizeBeforeUpdate = usoestacionRepository.findAll().size();

        // Update the usoestacion
        Usoestacion updatedUsoestacion = usoestacionRepository.findOne(usoestacion.getId());
        updatedUsoestacion
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .idEstacion(UPDATED_ID_ESTACION)
            .nombreEstacion(UPDATED_NOMBRE_ESTACION)
            .fechaDeUso(UPDATED_FECHA_DE_USO)
            .intervaloDeTiempo(UPDATED_INTERVALO_DE_TIEMPO)
            .devolucionTotal(UPDATED_DEVOLUCION_TOTAL)
            .devolucionMedia(UPDATED_DEVOLUCION_MEDIA)
            .retiradasTotal(UPDATED_RETIRADAS_TOTAL)
            .retiradasMedia(UPDATED_RETIRADAS_MEDIA)
            .neto(UPDATED_NETO)
            .total(UPDATED_TOTAL)
            .fechaObtencionDatos(UPDATED_FECHA_OBTENCION_DATOS)
            .ficheroCSV(UPDATED_FICHERO_CSV)
            .ficheroXLS(UPDATED_FICHERO_XLS)
            .hashcode(UPDATED_HASHCODE);
        UsoestacionDTO usoestacionDTO = usoestacionMapper.toDto(updatedUsoestacion);

        restUsoestacionMockMvc.perform(put("/api/usoestacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usoestacionDTO)))
            .andExpect(status().isOk());

        // Validate the Usoestacion in the database
        List<Usoestacion> usoestacionList = usoestacionRepository.findAll();
        assertThat(usoestacionList).hasSize(databaseSizeBeforeUpdate);
        Usoestacion testUsoestacion = usoestacionList.get(usoestacionList.size() - 1);
        assertThat(testUsoestacion.getNombreCompleto()).isEqualTo(UPDATED_NOMBRE_COMPLETO);
        assertThat(testUsoestacion.getIdEstacion()).isEqualTo(UPDATED_ID_ESTACION);
        assertThat(testUsoestacion.getNombreEstacion()).isEqualTo(UPDATED_NOMBRE_ESTACION);
        assertThat(testUsoestacion.getFechaDeUso()).isEqualTo(UPDATED_FECHA_DE_USO);
        assertThat(testUsoestacion.getIntervaloDeTiempo()).isEqualTo(UPDATED_INTERVALO_DE_TIEMPO);
        assertThat(testUsoestacion.getDevolucionTotal()).isEqualTo(UPDATED_DEVOLUCION_TOTAL);
        assertThat(testUsoestacion.getDevolucionMedia()).isEqualTo(UPDATED_DEVOLUCION_MEDIA);
        assertThat(testUsoestacion.getRetiradasTotal()).isEqualTo(UPDATED_RETIRADAS_TOTAL);
        assertThat(testUsoestacion.getRetiradasMedia()).isEqualTo(UPDATED_RETIRADAS_MEDIA);
        assertThat(testUsoestacion.getNeto()).isEqualTo(UPDATED_NETO);
        assertThat(testUsoestacion.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testUsoestacion.getFechaObtencionDatos()).isEqualTo(UPDATED_FECHA_OBTENCION_DATOS);
        assertThat(testUsoestacion.getFicheroCSV()).isEqualTo(UPDATED_FICHERO_CSV);
        assertThat(testUsoestacion.getFicheroXLS()).isEqualTo(UPDATED_FICHERO_XLS);
        assertThat(testUsoestacion.getHashcode()).isEqualTo(UPDATED_HASHCODE);
    }

    @Test
    @Transactional
    public void updateNonExistingUsoestacion() throws Exception {
        int databaseSizeBeforeUpdate = usoestacionRepository.findAll().size();

        // Create the Usoestacion
        UsoestacionDTO usoestacionDTO = usoestacionMapper.toDto(usoestacion);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUsoestacionMockMvc.perform(put("/api/usoestacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usoestacionDTO)))
            .andExpect(status().isCreated());

        // Validate the Usoestacion in the database
        List<Usoestacion> usoestacionList = usoestacionRepository.findAll();
        assertThat(usoestacionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUsoestacion() throws Exception {
        // Initialize the database
        usoestacionRepository.saveAndFlush(usoestacion);
        int databaseSizeBeforeDelete = usoestacionRepository.findAll().size();

        // Get the usoestacion
        restUsoestacionMockMvc.perform(delete("/api/usoestacions/{id}", usoestacion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Usoestacion> usoestacionList = usoestacionRepository.findAll();
        assertThat(usoestacionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Usoestacion.class);
        Usoestacion usoestacion1 = new Usoestacion();
        usoestacion1.setId(1L);
        Usoestacion usoestacion2 = new Usoestacion();
        usoestacion2.setId(usoestacion1.getId());
        assertThat(usoestacion1).isEqualTo(usoestacion2);
        usoestacion2.setId(2L);
        assertThat(usoestacion1).isNotEqualTo(usoestacion2);
        usoestacion1.setId(null);
        assertThat(usoestacion1).isNotEqualTo(usoestacion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsoestacionDTO.class);
        UsoestacionDTO usoestacionDTO1 = new UsoestacionDTO();
        usoestacionDTO1.setId(1L);
        UsoestacionDTO usoestacionDTO2 = new UsoestacionDTO();
        assertThat(usoestacionDTO1).isNotEqualTo(usoestacionDTO2);
        usoestacionDTO2.setId(usoestacionDTO1.getId());
        assertThat(usoestacionDTO1).isEqualTo(usoestacionDTO2);
        usoestacionDTO2.setId(2L);
        assertThat(usoestacionDTO1).isNotEqualTo(usoestacionDTO2);
        usoestacionDTO1.setId(null);
        assertThat(usoestacionDTO1).isNotEqualTo(usoestacionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(usoestacionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(usoestacionMapper.fromId(null)).isNull();
    }
}
