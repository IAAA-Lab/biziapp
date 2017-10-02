/**
 * Clase para el tratamiento de los ficheros .xls y su conversión a .csv.
 * El fichero de entrada es una hoja de cálculo que contiene la información
 * extraida de la web clearchannel.
 * El fichero de salida es un CSV a partir de los datos del fichero anterior. 
 */
package es.unizar.iaaa.bizi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;

public class TratamientoXLStoCSV {
	
	private static Configuracion config;
	private static String downloadPath;
	private static String registerPath;
	private static String csvPath;
	private static Herramientas herramienta;
	
	public static void main(String[] args) throws IOException, ParseException {

		config = new Configuracion();
		downloadPath = System.getProperty("user.dir") + config.getDownloadPath();
		registerPath = System.getProperty("user.dir") + config.getLogPath();
		csvPath = System.getProperty("user.dir") + config.getCsvPath();
		// Comprobar que la carpeta donde se generan los logs existe
		File registerDirectory = new File(registerPath);
		if (!registerDirectory.exists()) {
			// Si no existe se crea
			registerDirectory.mkdir();
		}

		// Comprobar que la carpeta donde se guardaran los CSV existe
		File csvDirectory = new File(csvPath);
		if (!csvDirectory.exists()) {
			// Si no existe se crea
			csvDirectory.mkdir();
		}

		herramienta = new Herramientas();

		String registerDownloadPath = System.getProperty("user.dir") + config.getLogPath()
				+ System.getProperty("file.separator") + "download.log";

		// Lectura del fichero log en busca de alguna descarga fallida anterior
		File downloadFile = new File(registerDownloadPath);

		// Leer fichero download.log y obtener lista de ficheros a tratar
		if (downloadFile.exists()) {
			ArrayList<JSONObject> lista = herramienta.obtenerEntradas(downloadFile);

			// Si existen fichero a tratar:
			if (!lista.isEmpty()) {
				// Recorrer lista
				for (int i = 0; i < lista.size(); i++) {
					// Obtener el objeto JSON
					JSONObject jsonObject = lista.get(i);
					// Extraer la fecha del fichero que se va a descargar
					String pathFicheroXLS = (String) jsonObject.get("PathCompleto");
					System.out.println(pathFicheroXLS);
					String tipoFichero = (String) jsonObject.get("Tipo");
					System.out.println(tipoFichero);
					// Realizar el tratamiento TODO: Habra que modificar para cuando hayan otros tipos descargados
					int result = tratarXLS(pathFicheroXLS, tipoFichero);
//					int result = usoEstacion.descargar(fecha);
					// Si el tratamiento se desarrolla correctamente
//					if(result==1) {
//						// Eliminar la linea del fichero
//						eliminarLineaFichero(fichero, lista.get(i));
//					}
				}
			}

		}

	}

	/**
	 * Tratamiento de los ficheros xls para convertirlos en csv. A parte del
	 * contenido obtenido del xls, se le aniaden otros elementos para enriquecer el
	 * fichero.
	 * 
	 * @param pathFicheroXLS
	 *            Ruta completa del fichero xls que va a ser tratado.
	 * @param tipoDatosFichero
	 *            Tipo de fichero correspondiente a su contenido.
	 * @return
	 */
	private static int tratarXLS(String pathFicheroXLS, String tipoDatosFichero) {
		return 1;
		
		
	}
	

	
}
