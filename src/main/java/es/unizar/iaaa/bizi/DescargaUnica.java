package es.unizar.iaaa.bizi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
	
	public static void main(String[] args) throws IOException, ParseException,InterruptedException {

		// Desde aqui se llamará a las descargas diarias de ficheros, indicando la fecha
		// a descargar
		// La respuesta obtenida desde la gestion de la descarga será almacenada en un
		// fichero log.
		// En caso de que el fichero log contenga entradas de descargas fallidas, se
		// intentaran descargar
		// una vez descargada la del día actual

		// Llamar a metodo de descarga y recoger lo que devuelve
		UsoEstaciones usoEstacion = new UsoEstaciones();
//		String fecha = obtenerFecha();
//		usoEstacion.descargar(fecha);
		

		Configuracion config = new Configuracion();
		String registerErrorPath = System.getProperty("user.dir") + config.getLogPath() + System.getProperty("file.separator") + "error.log";
		
		// Lectura del fichero log en busca de alguna descarga fallida anterior
		File errorFile = new File(registerErrorPath);
		ArrayList<String> fechas = new ArrayList<>();
		String line = null;
		JSONParser parser = new JSONParser();
		if(errorFile.exists()) {
			
			
			FileReader fr = new FileReader(errorFile);
			BufferedReader bf = new BufferedReader(fr);
			
			
			
			// lectura del fichero y obtener las fechas de los ficheros que se van a volver
			// a descargar
			while((line = bf.readLine())!=null) {
				// Tratar la linea como un objeto JSON
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				// Añadir al ArrayList las fechas
				fechas.add((String) jsonObject.get("FechaFichero"));
			}
			bf.close();
			fr.close();
			
		}
		// Si existe, realizar descarga
		if(!fechas.isEmpty()) {
			for (int i=0; i< fechas.size(); i++) {
				int result = usoEstacion.descargar(fechas.get(i));
				// Si devuelve que la operación se ha realizado de forma correcta
				if(result==1) {
					FileReader fr = new FileReader(errorFile);
					BufferedReader bf = new BufferedReader(fr);
					
					// Se elimina la entrada del fichero log
					String borrar = null;
					while((line = bf.readLine())!=null) {
						// Tratar la linea como un objeto JSON
						Object obj = parser.parse(line);
						JSONObject jsonObject = (JSONObject) obj;
						System.out.println(jsonObject.toString());
						String fechaComprobar = (String) jsonObject.get("FechaFichero");
						String reg = (String) jsonObject.get("Registro");
						System.out.println(fechaComprobar);
						if(fechaComprobar.equals(fechas.get(i))) {
							System.out.println("SOMOS IGUALES");
							borrar = "sed -i '/" + reg + "/d' " + errorFile;
							System.out.println(borrar);
							// GUARDAR CADENA EN ARRAYLIST
						}
					}
					bf.close();
					fr.close();
					// REESCRIBIR EL FICHERO EN OTRO Y BORRAR EL ANTERIOR
				}
			}			
		}
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
