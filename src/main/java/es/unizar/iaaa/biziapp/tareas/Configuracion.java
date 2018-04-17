package es.unizar.iaaa.biziapp.tareas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

public interface Configuracion {

	/**
	 * Obtiene parametros de login del fichero de configuracion JSON
	 * @return ["user","password"]
	 */
	ArrayList<String> getCredenciales();
	/**
	 * Obtiene la URL de acceso al servicio web del fichero de configuracion JSON
	 * @return url
	 */
	String getBaseURL();
	/**
	 * Obtiene la ruta donde se encuentra el fichero ChromeDrive del fichero de configuracion JSON
	 * @return ruta completa hasta el fichero ChromeDriver. Ej Linux: /home/herramientas/chromedriver
	 */
	String getChromeDriverLocation();

	/**
	 * Obtiene la ruta donde se almacenan los ficheros descargados del fichero de configuracion JSON
	 * @return ruta completa de la carpeta de descargas
	 */
	String getDownloadPath();

	/**
	 * Obtiene la ruta donde se guardan los ficheros csv del fichero de configuracion JSON
	 * @return ruta completa de la carpeta de ficheros csv
	 */
	String getCsvPath();
	/**
	 * Obtiene la ruta de la carpeta que se encuentra compartida entre el sistema host y el docker
	 * @return ruta completa de la carpeta compartida
	 */
	String getDockerSharedDirectory();

	/**
	 * Genera la cadena necesaria para realizar la conexión con JDBC a partir de un fichero JSON
	 * @return ej: jdbc:hive2//host:port/dbName
	 */
	String getJdbcHiveConnector();

	/**
	 * Obtiene parametros de login para la base de datos del fichero de configuracion JSON
	 * @return "user":"password"
	 */
	String getCredentialHiveDB();

	/**
	 * Obtiene la cadena correspondiente con el nombre del driver de la base de datos usada.
	 * @return ej: "org.apache.hive.jdbc.HiveDriver"
	 */
	String getDriverNameHiveDB();

	/**
	 * Genera la cadena necesaria para realizar la conexión con JDBC a partir de un fichero JSON
	 * @return ej: jdbc:mysql//host:port/dbName
	 */
	String getJdbcMysqlConnector();

	/**
	 * Obtiene parametros de login para la base de datos del fichero de configuracion JSON
	 * @return "user:password"
	 */
	String getCredentialMysqlDB();

	/**
	 * Obtiene la cadena correspondiente con el nombre del driver de la base de datos usada.
	 * @return ej: "com.mysql.jdbc.Driver"
	 */
	String getDriverNameMysqlDB();

	boolean isDockerMode();
}
