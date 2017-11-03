package es.unizar.iaaa.bizi.v0;
//package es.unizar.iaaa.bizi;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.FileSystems;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//
//import org.json.simple.JSONObject;
//import org.json.simple.parser.ParseException;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//
//public class InsertarBD {
//
//	private static Configuracion config;
//	private static String registerPath;
//	private static String csvPath;
//	private static Herramientas herramienta;
//	private static String dockerSharedPath;
//	private static String driverNameDB, jdbcConnector, userDB, passwordDB;
//	
//	public static void main(String[] args) throws IOException, ParseException {
//		
//		config = new Configuracion();
//		jdbcConnector = config.getJdbcHiveConnector();
//		driverNameDB = config.getDriverNameHiveDB();
//		String[] credentialDB = config.getCredentialHiveDB().split(":");
//		userDB = credentialDB[0];
//		passwordDB = credentialDB[1];
//		registerPath = config.getLogPath();
//		csvPath = config.getCsvPath();
//		dockerSharedPath = config.getDockerSharedDirectory();
//		
//		// Comprobar que la carpeta donde se generan los logs existe
//		File registerDirectory = new File(registerPath);
//		if (!registerDirectory.exists()) {
//			// Si no existe se crea
//			registerDirectory.mkdir();
//		}
//		
//		// Comprobar que la carpeta donde se guardaran los CSV existe
//		File csvDirectory = new File(csvPath);
//		if (!csvDirectory.exists()) {
//			// Si no existe se crea
//			csvDirectory.mkdir();
//		}
//		
//		// Comprobar que la carpeta compartida con el docker existe
//		File dockerSharedDirectory = new File(dockerSharedPath);
//		if (!dockerSharedDirectory.exists()) {
//			// Si no existe se crea
//			dockerSharedDirectory.mkdir();
//		}
//		herramienta = new Herramientas();
//
//		String registerTransformedPath = config.getLogPath() + System.getProperty("file.separator") + "transformed.log";
//		
//		// Leer fichero de log de ficheros ya convertidos a CSV(transformed.log)
//		File transformedLogFile = new File(registerTransformedPath);
//		
//		if(transformedLogFile.exists()) {
//			ArrayList<JSONObject> lista = herramienta.obtenerEntradas(transformedLogFile);
//			
//			if(!lista.isEmpty()) {
//				for (int i = 0; i < lista.size(); i++) {
//					// Obtener el objeto JSON
//					JSONObject jsonObject = lista.get(i);
//					// Realizar la insercion TODO: Habra que modificar para cuando hayan otros tipos descargados
//					int result = insertarDatos(jsonObject);
//					// Si la insercion se desarrolla correctamente
//					if(result==1) {
//					// Eliminar la linea del fichero
//						herramienta.eliminarLineaFichero(transformedLogFile, lista.get(i));
//					}
//				}	
//			}
//		}
//	}
//	
//	private static int insertarDatos(JSONObject jsonObject) throws JsonProcessingException {
//		String pathFicheroCSV = (String) jsonObject.get("PathCompleto");
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		/*
//		 * Copiar fichero a la carpeta compartida del docker, para poder realizar la
//		 * insercion de los datos.
//		 */
//		// Nombre del fichero que se ha tratado
//		String nombreFicheroCSV = pathFicheroCSV.substring(pathFicheroCSV.lastIndexOf(System.getProperty("file.separator")) + 1);
//		Path origenPath = FileSystems.getDefault().getPath(pathFicheroCSV);
//		Path destPath = FileSystems.getDefault().getPath(dockerSharedPath + System.getProperty("file.separator") + nombreFicheroCSV);
//		
//		try {
//			Files.copy(origenPath, destPath,StandardCopyOption.REPLACE_EXISTING);
//		} catch (IOException e1) {
////			e1.printStackTrace();
//			return -1;
//		}
//		
//		try {
//			Class.forName(driverNameDB);
//		} catch (ClassNotFoundException e) {
//			// Eliminar fichero de la carpeta compartida del docker
//			File fileToDelete = new File(destPath.toString());
//			fileToDelete.delete();
////			e.printStackTrace();
//			return -1;
//		}
//		// Conectar con la base de datos
//		try (Connection con = DriverManager.getConnection(jdbcConnector, userDB, passwordDB);) {
//			
//			Statement stmt = con.createStatement();
//
//			// Crear base de datos si no existe
//			String createBD = "CREATE DATABASE IF NOT EXISTS BIZIAPP";
//			stmt.execute(createBD);
//			
//			// Crear tabla temporal para la carga de los nuevos datos.
//			String nombreTablaUsoEstacionesTmp = "BIZIAPP.USOESTACIONESTMP";
//			// Eliminar la tabla temporal si exite.
//			stmt.execute("drop table if exists " + nombreTablaUsoEstacionesTmp);
//						
//			String createTableUsoEstacionesTmp = "create table if not exists " + nombreTablaUsoEstacionesTmp
//					+ "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso DATE,"
//					+ "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
//					+ "retiradasMedia float, neto float, total float, fechaObtencionDatos DATE, ficheroCSV  String,"
//					+ "ficheroXLS  String) " + "row format delimited fields terminated by \",\" "
//					+ "lines terminated BY \'\n\' " + "tblproperties(\"skip.header.line.count\"=\"1\")";
//
//			stmt.execute(createTableUsoEstacionesTmp);
//
//			// Generar Path que sea congruente con el sistema de ficheros del docker
//			String pathFileInDocker = System.getProperty("file.separator") + dockerSharedPath.substring(dockerSharedPath.lastIndexOf(System.getProperty("file.separator"))+1);
//			pathFileInDocker += System.getProperty("file.separator") +nombreFicheroCSV;
//			
//			// Carga de datos en la tabla auxiliar
//			String loadDataCSV = "LOAD DATA LOCAL INPATH '" + pathFileInDocker + "' INTO TABLE "
//					+ nombreTablaUsoEstacionesTmp;
//			stmt.execute(loadDataCSV);
//			/*
//			 * IMPORTANTE: NO ES NECESARIO EL PASO DE CONVERTIR A ORC.
//			 * 			HAY QUE ELIMINAR LOS PASOS SIGUIENTES
//			 * 			PERO HAY QUE TENER CUIDADO CON EL TEMA DE INSERTAR ENTRADAS REPETIDAS
//			 */
//			// Trasladar los datos de la tabla anterior a una table con formato de almacenamiento ORC
//			String nombreTablaUsoEstacionesORC = "BIZIAPP.USOESTACIONES";
//			String createTableUsoEstacionesORC = "create table if not exists " + nombreTablaUsoEstacionesORC
//					+ "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso DATE,"
//					+ "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
//					+ "retiradasMedia float, neto float, total float, fechaObtencionDatos DATE, ficheroCSV  String,"
//					+ "ficheroXLS  String)";
//			
//			stmt.execute(createTableUsoEstacionesORC);
//			
//			/*
//			 * Insertar solo registros que no esten contenidas.
//			 * Evitar que se inserten registros ya procesados.
//			 * Se usa el nombre del ficheroXL de donde provienen los datos originales,
//			 * ya que sirve como un identificador unico.
//			 */
//			String selectControl = "select ficheroXLS from " + nombreTablaUsoEstacionesORC;
//			String selectAinsertar = "select * from " + nombreTablaUsoEstacionesTmp + " tmp where tmp.ficheroXLS not in ";
//			String insertDataORCsinRepetidos = "INSERT INTO TABLE " + nombreTablaUsoEstacionesORC + " " + selectAinsertar + "(" + selectControl + ")";  
//			
//			stmt.execute(insertDataORCsinRepetidos);
//			
//			// Eliminar la tabla temporal
////			stmt.execute("drop table if exists " + nombreTablaUsoEstacionesTmp);
//			con.close();
//			
//			
//			// Eliminar fichero de la carpeta compartida del docker
//			File fileToDelete = new File(destPath.toString());
//			fileToDelete.delete();
//			
//			// Generar entrada en el fichero log correspondiente
//			String fechaFichero = (String) jsonObject.get("FechaFichero");
//			herramienta.generarLog(timestamp, Estado.SUCCESStoHADOOP, fechaFichero, pathFicheroCSV, "Uso de las estaciones",
//					"3.1-Usos de las estaciones", Tipo.USOESTACION);
//			return 1;
//			
//		} catch (SQLException e) {
//			// Eliminar fichero de la carpeta compartida del docker
//			File fileToDelete = new File(destPath.toString());
//			fileToDelete.delete();
////			e.printStackTrace();
//			return -1;
//		}
//
//	}
//
//}
