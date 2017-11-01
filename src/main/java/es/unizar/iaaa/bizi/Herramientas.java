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
import java.io.PrintWriter;
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
	private static String registerPath;

	public Herramientas() {
		config = new Configuracion();
		registerPath = config.getLogPath();
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
	
	/**
	 * Elimina del fichero la linea que se pasa por parametro
	 * @param fichero fichero del que se eliminara la fila
	 * @param lineToRemove linea representada por un objeto JSON que se quiere eliminar del fichero
	 * @throws IOException
	 */
	protected void eliminarLineaFichero(File fichero, JSONObject lineToRemove) throws IOException {
		
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
	
	/**
	 * Renombrar el fichero anadiendole la fecha de la información que contiene
	 * 
	 * @param downloadPath
	 *            donde se encuentra el fichero a renombrar
	 * @param nombreFichero
	 *            nombre que tiene el fichero a renombrar
	 * @param fecha
	 *            fecha que se quiere anadir al nombre (formato "dd/MM/yyyy")
	 * @return Ruta absoluta del fichero renombrado
	 */
	public String renameFile(String downloadPath, String nombreFichero, String fecha) {
		// Obtener path completo del fichero
		String path = downloadPath + System.getProperty("file.separator") + nombreFichero;
		File fichero = new File(path);
		String result = null;

		// Comprobar si existe el fichero
		if (fichero.exists()) {
			// Obtener nombre sin extension
			String nombreSinExt = nombreFichero.substring(0, nombreFichero.lastIndexOf("."));
			// Anadir al nombre la fecha y de nuevo la extension
			String nuevoNombre = nombreSinExt + fecha.replaceAll("/", "") + ".xls";
			// Renombrar
			File dest = new File(downloadPath + System.getProperty("file.separator") + nuevoNombre);
			fichero.renameTo(dest);
			result = dest.getAbsolutePath();
		}
		return result;
	}
	
	/**
	 * Comprueba si un fichero existe
	 * @param pathCompleto 
	 * @return 1 = fichero existe. -1 = fichero no existe
	 */
	public int comprobarFichero(String pathCompleto) {
        int result=-1;
        File fichero = new File(pathCompleto);
        if (fichero.exists()) {
            result = 1;
        } else {
            result = -1;
        }
        return result;
    }
	
}
