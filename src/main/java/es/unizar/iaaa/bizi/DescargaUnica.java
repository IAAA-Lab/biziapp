package es.unizar.iaaa.bizi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.htrace.fasterxml.jackson.databind.node.ObjectNode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class DescargaUnica {
	
	private static UsoEstaciones usoEstacion;
	private static Herramientas herramienta;
	
	public static void main(String[] args) throws IOException, ParseException,InterruptedException {
		
//		 Desde aqui se llamará a las descargas diarias de ficheros, indicando la fecha
//		 a descargar
//		 La respuesta obtenida desde la gestion de la descarga será almacenada en un
//		 fichero log.
//		 En caso de que el fichero log contenga entradas de descargas fallidas, se
//		 intentaran descargar
//		 una vez descargada la del día actual

		herramienta = new Herramientas();
		// Llamar a metodo de descarga y recoger lo que devuelve
		usoEstacion = new UsoEstaciones();
		String fecha = obtenerFecha();
		usoEstacion.descargar(fecha);
		
		// Descargar ficheros que han fallado en otro momento
		Configuracion config = new Configuracion();
		String registerErrorPath = System.getProperty("user.dir") + config.getLogPath() + System.getProperty("file.separator") + "error.log";
		
		// Lectura del fichero log en busca de alguna descarga fallida anterior
		File errorFile = new File(registerErrorPath);
		
		// Comprobar si el fichero existe
		if(errorFile.exists()) {
			repetirDescarga(errorFile);
		}
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
	
	/**
	 * Repite la descarga de los elementos que se encuentren registrados en el fichero.
	 * Si la descarga se realiza correctamente, elimina la respectiva entrada del fichero.
	 * @param fichero Fichero del que se extraen los elementos que deben volver a descargarse
	 * @throws IOException
	 * @throws ParseException
	 */
	private static void repetirDescarga(File fichero) throws IOException, ParseException {
		// Crear lista con los ficheros que se van a intentar descargar
		ArrayList<JSONObject> lista = herramienta.obtenerEntradas(fichero);
		
		// Recorrer lista
		for (int i = 0; i < lista.size(); i++) {
			// Obtener el objeto JSON
			JSONObject jsonObject = lista.get(i);
			// Extraer la fecha del fichero que se va a descargar
			String fecha = (String) jsonObject.get("FechaFichero");
			// Realizar la descarga
			int result = usoEstacion.descargar(fecha);
			// Si la descarga se desarrolle correctamente
			if(result==1) {
				// Eliminar la linea del fichero
				eliminarLineaFichero(fichero, lista.get(i));
			}
		}

	}
	
	/**
	 * Elimina del fichero la linea que se pasa por parametro
	 * @param fichero fichero del que se eliminara la fila
	 * @param lineToRemove linea representada por un objeto JSON que se quiere eliminar del fichero
	 * @throws IOException
	 */
	private static void eliminarLineaFichero(File fichero, JSONObject lineToRemove) throws IOException {
		
		FileReader fr = new FileReader(fichero);
		BufferedReader br = new BufferedReader(fr);
		// Crear un fichero temporar para volcar las lineas que se desean conservar
		File tempFile = new File(fichero.getAbsolutePath() + ".tmp");
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		
		String linea = null;
		// Obtener el parametro "id" del objeto JSON
		String id = (String) lineToRemove.get("id");
		while((linea = br.readLine()) != null) {
			// Comprobar si la linea leida contiene el id del objeto JSON a eliminar
			if(!linea.contains(id)) {
				//En caso de no coincidir, copiar la linea al fichero temporal
				pw.println(linea);
				pw.flush();
			}
		}
		br.close();
		pw.close();
		// Renombrar fichero temporal como fichero legitimo
		fichero.delete();
		tempFile.renameTo(fichero);
	}
	
}
