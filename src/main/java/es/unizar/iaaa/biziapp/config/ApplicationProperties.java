package es.unizar.iaaa.biziapp.config;

import es.unizar.iaaa.biziapp.tareas.Configuracion;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties implements Configuracion{

    @PostConstruct
    public void postConstruct(){
        System.out.println("\n\n\n"
            +getCredenciales()+"\n"
            +getBaseURL()+"\n"
            +getChromeDriverLocation()+"\n"
            +getDownloadPath()+"\n"
            +"\n\n");
    }

    private String legacysystemCredenciales;
    private String legacysystemBaseUrl;

    private String pathChromeDriver;
    private String pathDownload;
    private String pathCsv;
    private String pathDockerShared;

    private String hiveUrl;
    private String hiveCredenciales;
    private String hiveJdbc;

    private String mysqlUrl;
    private String mysqlCredenciales;
    private String mysqlJdbc;

    private boolean docker;


    /**
     * Devuelve si la aplicación se está ejecutando en modo docker o no
     * @return true/false
     */
    public boolean isDockerMode() {
        return docker;
    }

    /**
     * Obtiene parametros de login del fichero de configuracion JSON
     * @return ["user","password"]
     */
    public ArrayList<String> getCredenciales() {
        ArrayList<String> result = new ArrayList<>();
//		JSONObject login = (JSONObject) jsonObject.get("legacySystem");
//		String user = login.get("user").toString();
//		String password = login.get("password").toString();
//		result.add(user);
//		result.add(password);

        result.add(legacysystemCredenciales.split(":")[0]);
        result.add(legacysystemCredenciales.split(":")[1]);
        return result;
    }
    /**
     * Obtiene la URL de acceso al servicio web del fichero de configuracion JSON
     * @return url
     */
    public String getBaseURL() {
//		JSONObject legacySystem = (JSONObject) jsonObject.get("legacySystem");
//        return legacySystem.get("baseURL").toString();
        return legacysystemBaseUrl;
    }
    /**
     * Obtiene la ruta donde se encuentra el fichero ChromeDrive del fichero de configuracion JSON
     * @return ruta completa hasta el fichero ChromeDriver. Ej Linux: /home/herramientas/chromedriver
     */
    public String getChromeDriverLocation() {
//		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
//        return userDir + fileLocation.get("chromeDriverLocation").toString();
        return pathChromeDriver;
    }

    /**
     * Obtiene la ruta donde se almacenan los ficheros descargados del fichero de configuracion JSON
     * @return ruta completa de la carpeta de descargas
     */
    public String getDownloadPath() {
//		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
//        return userDir + fileLocation.get("downloadLocation").toString();
        return pathDownload;
    }

    /**
     * Obtiene la ruta donde se guardan los ficheros csv del fichero de configuracion JSON
     * @return ruta completa de la carpeta de ficheros csv
     */
    public String getCsvPath() {
//		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
//        return userDir + fileLocation.get("csvDirectory").toString();
        return pathCsv;
    }
    /**
     * Obtiene la ruta de la carpeta que se encuentra compartida entre el sistema host y el docker
     * @return ruta completa de la carpeta compartida
     */
    public String getDockerSharedDirectory() {
//		JSONObject fileLocation = (JSONObject) jsonObject.get("fileLocation");
//        return userDir + fileLocation.get("dockerSharedDirectory").toString();
        return pathDockerShared;
    }

    /**
     * Genera la cadena necesaria para realizar la conexión con JDBC a partir de un fichero JSON
     * @return ej: jdbc:hive2//host:port/dbName
     */
    public String getJdbcHiveConnector() {
//		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionHiveDB");
//		String jdbc = connectionDB.get("jdbc").toString();
//		String host = connectionDB.get("host").toString();
//		String port = connectionDB.get("port").toString();
//		String database = connectionDB.get("database").toString();
//        return jdbc + host + ":" + port + "/" + database;
        return hiveUrl;
    }

    /**
     * Obtiene parametros de login para la base de datos del fichero de configuracion JSON
     * @return "user":"password"
     */
    public String getCredentialHiveDB() {
//		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionHiveDB");
//		String user = connectionDB.get("user").toString();
//		String password = connectionDB.get("password").toString();
//        return user + ":" + password;
        return hiveCredenciales;
    }

    /**
     * Obtiene la cadena correspondiente con el nombre del driver de la base de datos usada.
     * @return ej: "org.apache.hive.jdbc.HiveDriver"
     */
    public String getDriverNameHiveDB() {
//		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionHiveDB");
//        return connectionDB.get("driverName").toString();
        return hiveJdbc;
    }

    /**
     * Genera la cadena necesaria para realizar la conexión con JDBC a partir de un fichero JSON
     * @return ej: jdbc:mysql//host:port/dbName
     */
    public String getJdbcMysqlConnector() {
//		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionMysqlDB");
//		String jdbc = connectionDB.get("jdbc").toString();
//		String host = connectionDB.get("host").toString();
//		String port = connectionDB.get("port").toString();
//		String database = connectionDB.get("database").toString();
//        return jdbc + host + ":" + port + "/" + database;
        return mysqlUrl;
    }

    /**
     * Obtiene parametros de login para la base de datos del fichero de configuracion JSON
     * @return "user:password"
     */
    public String getCredentialMysqlDB() {
//		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionMysqlDB");
//        String user = connectionDB.get("user").toString();
//        String password = connectionDB.get("password").toString();
//        return user + ":" + password;
        return mysqlCredenciales;
    }

    /**
     * Obtiene la cadena correspondiente con el nombre del driver de la base de datos usada.
     * @return ej: "com.mysql.jdbc.Driver"
     */
    public String getDriverNameMysqlDB() {
//		JSONObject connectionDB = (JSONObject) jsonObject.get("connectionMysqlDB");
//        return connectionDB.get("driverName").toString();
        return mysqlJdbc;
    }



    /*
    SETTERS NEEDED BY ConfigurationProperties
     */

    public void setLegacysystemCredenciales(String legacysystemCredenciales) {
        this.legacysystemCredenciales = legacysystemCredenciales;
    }

    public void setLegacysystemBaseUrl(String legacysystemBaseUrl) {
        this.legacysystemBaseUrl = legacysystemBaseUrl;
    }

    public void setPathChromeDriver(String pathChromeDriver) {
        this.pathChromeDriver = pathChromeDriver;
    }

    public void setPathDownload(String pathDownload) {
        this.pathDownload = pathDownload;
    }

    public void setPathCsv(String pathCsv) {
        this.pathCsv = pathCsv;
    }

    public void setPathDockerShared(String pathDockerShared) {
        this.pathDockerShared = pathDockerShared;
    }

    public void setHiveUrl(String hiveUrl) {
        this.hiveUrl = hiveUrl;
    }

    public void setHiveCredenciales(String hiveCredenciales) {
        this.hiveCredenciales = hiveCredenciales;
    }

    public void setHiveJdbc(String hiveJdbc) {
        this.hiveJdbc = hiveJdbc;
    }

    public void setMysqlUrl(String mysqlUrl) {
        this.mysqlUrl = mysqlUrl;
    }

    public void setMysqlCredenciales(String mysqlCredenciales) {
        this.mysqlCredenciales = mysqlCredenciales;
    }

    public void setMysqlJdbc(String mysqlJdbc) {
        this.mysqlJdbc = mysqlJdbc;
    }

    public void setDocker(boolean docker) {
        this.docker = docker;
    }
}
