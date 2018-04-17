package es.unizar.iaaa.biziapp.tareas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dani on 28/10/17.
 */
@Component
public class Gestor {
    private static final Logger log = LoggerFactory.getLogger(Gestor.class);

    @Autowired
    private GeneradorHistorico generadorHistorico;
    @Autowired
    private GeneradorFechas generadorFechas;
    @Autowired
    private DescargarFichero descargarFichero;
    @Autowired
    private TratamientoFicheros tratamientoFicheros;
    @Autowired
    private InsertarHadoop insertarHadoop;

    // Lanzar solo una vez al iniciar
    public void generarHistorico(){
        log.info("Lanzando generador de historico");
        generadorHistorico.generarHistoricoUsoEstacion();
        log.info("Finalizado generador de historico");
    }

    // Lanzar Generador de fechas (Lanzado a las 00:30 todos los dias)
    @Scheduled(cron = "0 4 * * * *")
    public void generarFecha(){
        log.info("Lanzando generador de fecha");
        generadorFechas.generarFechasUsoEstacion();
        log.info("Finalizado generador de fecha");
    }

    // Lanzar descarga de ficheros (Lanzado cada 30 minutos)
    @Scheduled(cron = "0 0,30 * * * *")
    public void descargarFichero(){
        log.info("Lanzando descarga de fichero");
        descargarFichero.descargarFichero();
        log.info("Finalizada descarga de fichero");
    }

    // Lanzar conversor de ficheros
    @Scheduled(cron = "0 10,40 * * * *")
    public void tratamientoFichero(){
        log.info("Lanzando tratamiento de fichero");
        tratamientoFicheros.tratarFichero();
        log.info("Finalizado tratamiento de fichero");
    }

    // Lanzar inserción de datos a hadoop
    @Scheduled(cron = "0 20,50 * * * *")
    public void insertarBD(){
        log.info("Lanzando insercion en hadoop");
        insertarHadoop.insertarHadoop();
        log.info("Finalizada insercion en hadoop");
    }

}
