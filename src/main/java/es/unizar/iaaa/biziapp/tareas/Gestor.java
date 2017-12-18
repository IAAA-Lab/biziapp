package es.unizar.iaaa.biziapp.tareas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;



/**
 * Created by dani on 28/10/17.
 */
@Component
public class Gestor {
    private static final Logger log = LoggerFactory.getLogger(Gestor.class);
    private AtomicBoolean lock = new AtomicBoolean();
//    cron = "segundos minutos horas diaDelMes mes diaDeLaSemana"

//    @Scheduled(initialDelay = 2000, fixedRate = 500000)
    // Lanzar solo una vez al iniciar
    public void generarHistorico(){
        log.info("Lanzando generador de historico");
        GeneradorHistorico gh = new GeneradorHistorico();
        gh.generarHistoricoUsoEstacion();
        log.info("Finalizado generador de historico");
    }

    // Lanzar Generador de fechas (Lanzado a las 00:30 todos los dias)
//    @Scheduled(cron = "0 30 0 * * *")
    public void generarFecha(){
        log.info("Lanzando generador de fecha");
        GeneradorFechas gf = new GeneradorFechas();
        gf.generarFechasUsoEstacion();
        log.info("Finalizado generador de fecha");
    }

    // Lanzar descarga de ficheros (Lanzado cada 30 minutos)
//    @Scheduled(cron = "0 30 * * * *")
//    @Scheduled(initialDelay = 2000, fixedRate = 5000000)
    public void descargarFichero(){
        log.info("Lanzando descarga de fichero");
        DescargarFichero df = new DescargarFichero();
        df.descargarFichero();
        log.info("Finalizada descarga de fichero");
    }

    // Lanzar conversor de ficheros
//    @Scheduled(cron = "0 30 * * * *")
//    @Scheduled(initialDelay = 10000, fixedRate = 5000000)
    public void tratamientoFichero(){
        log.info("Lanzando tratamiento de fichero");
        TratamientoFicheros tf = new TratamientoFicheros();
        tf.tratarFichero();
        log.info("Finalizado tratamiento de fichero");
    }

    // Lanzar inserción de datos a hadoop
    //    @Scheduled(cron = "0 30 * * * *")
    public void insertarBD(){
        log.info("Lanzando insercion en hadoop");
        InsertarHadoop ih = new InsertarHadoop();
        ih.insertarHadoop();
        log.info("Finalizada insercion en hadoop");
    }

//    public void supervisorTareas(){
//        log.info("Lanzando supervisor de tareas");
//        Supervisor su = new Supervisor();
//
//        log.info("Finalizada supervision de tareas");
//    }

}
