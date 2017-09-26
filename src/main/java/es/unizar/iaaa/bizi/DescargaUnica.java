package es.unizar.iaaa.bizi;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DescargaUnica {

	public static void main(String[] args) {
	
		// Desde aqui se llamará a las descargas diarias de ficheros, indicando la fecha a descargar
		// La respuesta obtenida desde la gestion de la descarga será almacenada en un fichero log.
		// En caso de que el fichero log contenga entradas de descargas fallidas, se intentaran descargar
		// una vez descargada la del día actual
		
		// Llamar a metodo de descarga y recoger lo que devuelve
		UsoEstaciones usoEstacion = new UsoEstaciones();
		int result = usoEstacion.descargar(obtenerFecha());
		System.out.println(result);
		
		// incluir linea en fichero log
		if(result==1) {
			// incluir exito
		}else if(result==-1) {
			// incluir fallo
		}
		
		// Lectura del fichero log en busca de alguna descarga fallida anterior
		
		// Si existe, realizar descarga
		
		// Si la descarga es realizada, modificar fichero log
	}

	/**
	 * Obtener fecha del dia anterior (según la fecha del sistema) Hace de manera
	 * correcta la resta, incluso en años bisiesto.
	 * 
	 * @return fecha en formato "dd/MM/yyyy"
	 */
	private static String obtenerFecha() {
		String fecha = null;
		// Obtener fecha del dia anterior
		LocalDate date = LocalDate.now().plusDays(-1);
		// Dar formato de salida
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		fecha = date.format(formatter);
		return fecha;
	}
}
