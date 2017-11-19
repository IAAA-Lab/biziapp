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

//    public void prueba(){
//        log.info("Me intento Lanzar {}", lock);
////        if(lock.compareAndSet(false,true)){
//            log.info("Me lanzo {}", lock);
//            Pruebas p = new Pruebas();
//            p.main();
//            log.info("Acabo {}", lock);
////            lock.set(false);
////            log.info("Desbloqueado {}", lock);
////        }
//
//    }
//    @Scheduled(initialDelay = 2000, fixedRate = 500000)
    public void generarHistorico(){
        log.info("Lanzando generador de historico");
        GeneradorHistorico gh = new GeneradorHistorico();
        gh.generarHistoricoUsoEstacion();
        log.info("Finalizado generador de historico");
    }

    // Lanzar Generador de fechas
//    @Scheduled(cron = "0 0 17 * * *")
    public void generarFecha(){
        log.info("Lanzando generador de fecha");
        GeneradorFechas gf = new GeneradorFechas();
        gf.generarFechasUsoEstacion();
        log.info("Finalizado generador de fecha");
    }

    // Lanzar descarga de ficheros
//    @Scheduled(cron = "0 5 17 * * *")
//    @Scheduled(initialDelay = 2000, fixedRate = 5000000)
    public void descargarFichero(){
        log.info("Lanzando descarga de fichero");
        DescargarFichero df = new DescargarFichero();
        df.descargarFichero();
        log.info("Finalizada descarga de fichero");
    }

    // Lanzar conversor de ficheros
//    @Scheduled(cron = "0 10 17 * * *")
//    @Scheduled(initialDelay = 10000, fixedRate = 5000000)
    public void tratamientoFichero(){
        log.info("Lanzando tratamiento de fichero");
        TratamientoFicheros tf = new TratamientoFicheros();
        tf.tratarFichero();
        log.info("Finalizado tratamiento de fichero");
    }

    // Lanzar inserción de datos a hadoop
    public void insertarBD(){
        log.info("Lanzando insercion en hadoop");
        InsertarHadoop ih = new InsertarHadoop();
        ih.insertarHadoop();
        log.info("Finalizada insercion en hadoop");
    }

}
