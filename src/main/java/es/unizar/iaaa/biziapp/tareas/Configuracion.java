package es.unizar.iaaa.biziapp.tareas;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Configuracion {

	private JSONParser parser;
    private static JSONObject jsonObject;
	private static String userDir;

	public Configuracion() {
		userDir = System.getProperty("user.dir");
		parser = new JSONParser();
		try {
            Object ficheroJson = parser.parse(new FileReader(System.getProperty("user.dir") + "/myConfig.json"));
			jsonObject = (JSONObject) ficheroJson;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Obtiene parametros de login del fichero de configuracion JSON
	 * @return ["user","password"]
	 */
	protected ArrayList<String> getCredenciales() {
		ArrayList<String> result = new ArrayList<>();
		JSONObject login = (JSONObject) jsonObject.get("legacySystem");
		String user = login.get("user").toString();
		String password = login.get("password").toString();
		result.add(user);
		result.add(password);
		return result;
	}
	/**
	 * Obtiene la URL de acceso al servicio web del fichero de configuracion JSON
	 * @return url
	 */
	protected String getBaseURL() {
		JSONObject legacySystem = (JSONObject) jsonObject.get("legacySystem");
        return legacySystem.get("baseURL").toString();
	}
	/**
	 * Obtiene la ruta donde se encuentra el fichero ChromeDrive del fichero de configuracion JSON
	 * @return ruta completa hasta el fichero ChromeDriver. Ej Linux: /home/herramientas/chromedriver
	 */
	protected String getChromeDriverLocation() {
		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
        return fileLocation.get("chromeDriverLocation").toString();
	}

	/**
	 * Obtiene la ruta donde se almacenan los ficheros descargados del fichero de configuracion JSON
	 * @return ruta completa de la carpeta de descargas
	 */
	protected String getDownloadPath() {
		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
        return userDir + fileLocation.get("downloadLocation").toString();
	}

	/**
	 * Obtiene la ruta donde se guardan los ficheros csv del fichero de configuracion JSON
	 * @return ruta completa de la carpeta de ficheros csv
	 */
	protected String getCsvPath() {
		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
        return userDir + fileLocation.get("csvDirectory").toString();
	}
	/**
	 * Obtiene la ruta de la carpeta que se encuentra compartida entre el sistema host y el docker
	 * @return ruta completa de la carpeta compartida
	 */
	protected String getDockerSharedDirectory() {
		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
        return fileLocation.get("dockerSharedDirectory").toString();
	}

	/**
	 * Genera la cadena necesaria para realizar la conexión con JDBC a partir de un fichero JSON
	 * @return ej: jdbc:hive2//host:port/dbName
	 */
	protected String getJdbcHiveConnector() {
		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionHiveDB");
		String jdbc = connectionDB.get("jdbc").toString();
		String host = connectionDB.get("host").toString();
		String port = connectionDB.get("port").toString();
		String database = connectionDB.get("database").toString();
        return jdbc + host + ":" + port + "/" + database;
	}

	/**
	 * Obtiene parametros de login para la base de datos del fichero de configuracion JSON
	 * @return "user":"password"
	 */
	protected String getCredentialHiveDB() {
		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionHiveDB");
		String user = connectionDB.get("user").toString();
		String password = connectionDB.get("password").toString();
        return user + ":" + password;
	}

	/**
	 * Obtiene la cadena correspondiente con el nombre del driver de la base de datos usada.
	 * @return ej: "org.apache.hive.jdbc.HiveDriver"
	 */
	protected String getDriverNameHiveDB() {
		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionHiveDB");
        return connectionDB.get("driverName").toString();
	}

	/**
	 * Genera la cadena necesaria para realizar la conexión con JDBC a partir de un fichero JSON
	 * @return ej: jdbc:mysql//host:port/dbName
	 */
	protected String getJdbcMysqlConnector() {
		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionMysqlDB");
		String jdbc = connectionDB.get("jdbc").toString();
		String host = connectionDB.get("host").toString();
		String port = connectionDB.get("port").toString();
		String database = connectionDB.get("database").toString();
        return jdbc + host + ":" + port + "/" + database;
	}

	/**
	 * Obtiene parametros de login para la base de datos del fichero de configuracion JSON
	 * @return "user":"password"
	 */
	protected String getCredentialMysqlDB() {
		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionMysqlDB");
		String user = connectionDB.get("user").toString();
		String password = connectionDB.get("password").toString();
        return user + ":" + password;
	}

	/**
	 * Obtiene la cadena correspondiente con el nombre del driver de la base de datos usada.
	 * @return ej: "com.mysql.jdbc.Driver"
	 */
	protected String getDriverNameMysqlDB() {
		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionMysqlDB");
        return connectionDB.get("driverName").toString();
	}



}
