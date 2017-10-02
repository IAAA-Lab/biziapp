/**
 * Clase que contiene distintas herramientas a utilizar en varios de los procesos:
 * Tratamiento de ficheros.
 * Generacion de entradas en logs.
 */
package es.unizar.iaaa.bizi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Herramientas {
	private static Configuracion config;
	private static String downloadPath;
	private static String registerPath;

	public Herramientas() {
		config = new Configuracion();
		downloadPath = System.getProperty("user.dir") + config.getDownloadPath();
		registerPath = System.getProperty("user.dir") + config.getLogPath();
	}
	
	/**
	 * Genera una entrada en el fichero log correspondiente.
	 * El contenido de la linea es un objeto JSON.
	 * 
	 * @param registro
	 *            Timestamp de la operacion a registrar
	 * @param estado
	 *            Estado de la operacion a registrar
	 * @param fechaFichero
	 *            Fecha del contenido del fichero que se ha descargado o intentado
	 *            descargar
	 * @param pathCompleto
	 *            Ruta completa del fichero descargado, NULL en caso de error
	 * @param categoria
	 *            Representa la categoria del fichero descargado (definido por la
	 *            web clearchannel)
	 * @param subcategoria
	 *            Representa la subcategoria del fichero descargado (definido por la
	 *            web clearchannel)
	 * @param tipo Representa el tipo de fichero que se ha descargado 
	 * @throws JsonProcessingException
	 */
	protected void generarLog(Timestamp registro, Estado estado, String fechaFichero, String pathCompleto,
			String categoria, String subcategoria, Tipo tipo) throws JsonProcessingException {
		// regPath: ruta donde se encuentran los ficheros de log
		String regPath = registerPath + System.getProperty("file.separator");
		File myFile = null;
		File generalFile = new File(regPath+"general.log");
		
		// Generar mapa con los elementos de la entrada a log
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("id", (categoria+subcategoria+fechaFichero.replaceAll("/", "")).replaceAll(" ", ""));
		testMap.put("Tipo", tipo.toString());
		testMap.put("Registro", registro.toString());
		testMap.put("Estado", estado.toString());
		testMap.put("FechaFichero", fechaFichero);
		testMap.put("PathCompleto", pathCompleto);
		testMap.put("Categoria", categoria);
		testMap.put("Subcategoria", subcategoria);
		
		// Generar un string con formato JSON a partir del mapa
		String json = mapper.writeValueAsString(testMap);

		switch (estado) {
		case ERROR: 
			myFile = new File(regPath + "error.log");
			break;
		case SUCCESSDOWNLOAD:
			myFile = new File(regPath + "download.log");
			break;
		case SUCCESStoCSV:
			myFile = new File(regPath + "transformed.log");
			break;
		case SUCCESStoHADOOP:
			myFile = new File(regPath + "insertedHadoop.log");
			break;
		case SUCCESStoMYSQL:
			myFile = new File(regPath + "insertedMySQL.log");
			break;
		}
		
		// Realizar escritura en fichero log
		FileWriter fw;
		try {
			fw = new FileWriter(myFile, true);
			fw.write(json);
			fw.append("\n");
			fw.close();
			// Escribir la entrada en el fichero log general del sistema
			fw = new FileWriter(generalFile, true);
			fw.write(json);
			fw.append("\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Recupera las lineas (objetos JSON) del fichero, almacenandolas en una lista.
	 * 
	 * @param fichero
	 *            fichero de donde se extraen los objetos JSON
	 * @return lista de JSONObject que contiene las entradas, no repetidas,
	 *         obtenidas del fichero
	 * @throws IOException
	 * @throws ParseException
	 */
	protected ArrayList<JSONObject> obtenerEntradas(File fichero) throws IOException, ParseException {

		ArrayList<JSONObject> lista = new ArrayList<>();

		JSONParser parser = new JSONParser();
		FileReader fr = new FileReader(fichero);
		BufferedReader bf = new BufferedReader(fr);

		String line = null;
		// lectura del fichero y obtener el JSON de los ficheros que se van a volver
		// a descargar
		while ((line = bf.readLine()) != null) {
			// Tratar la linea como un objeto JSON
			Object obj = parser.parse(line);
			JSONObject jsonObject = (JSONObject) obj;

			// No insertar objetos repetidos (evitar que un mismo fichero se trate
			// varias veces)
			boolean noContenido = true;
			String idAinsertar = (String) jsonObject.get("id");
			for (int i = 0; i < lista.size(); i++) {
				String idLeido = (String) lista.get(i).get("id");
				if (idLeido.equals(idAinsertar)) {
					noContenido = false;
					break;
				}
			}
			if (noContenido) {
				lista.add(jsonObject);
			}
		}
		bf.close();
		fr.close();
		return lista;
	}
	
}
