/**
 * Clase para el tratamiento de los ficheros .xls y su conversión a .csv.
 * El fichero de entrada es una hoja de cálculo que contiene la información
 * extraida de la web clearchannel.
 * El fichero de salida es un CSV a partir de los datos del fichero anterior. 
 */
package es.unizar.iaaa.bizi;

import java.io.File;

import org.openqa.selenium.WebDriver;

public class TratamientoXLStoCSV {
	
	private static Configuracion config;
	private static String downloadPath;
	private static String registerPath;
	private static String csvPath;
	
	public TratamientoXLStoCSV() {
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
	}
	
	

	
}
