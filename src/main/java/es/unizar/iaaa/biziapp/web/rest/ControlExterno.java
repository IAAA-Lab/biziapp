package es.unizar.iaaa.biziapp.web.rest;

import es.unizar.iaaa.biziapp.security.AuthoritiesConstants;
import es.unizar.iaaa.biziapp.tareas.Gestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class ControlExterno {

    private final Logger log = LoggerFactory.getLogger(ControlExterno.class);

    @Autowired
    private Gestor gestor;

    @GetMapping("/historico")
    public void historico(){
        gestor.generarHistorico();
    }

    @GetMapping("/fecha")
    public void fecha(){
        gestor.generarFecha();
    }

    @GetMapping("/descargar")
    public void descargar(){
        gestor.descargarFichero();
    }

    @GetMapping("/procesar")
    public void procesar(){
        gestor.tratamientoFichero();
    }

    @GetMapping("/hadoop")
    public void hadoop(){
        gestor.insertarBD();
    }
}
