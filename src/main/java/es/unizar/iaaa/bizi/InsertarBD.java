package es.unizar.iaaa.bizi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class InsertarBD {

	private static Configuracion config;
	private static String registerPath;
	private static String csvPath;
	private static Herramientas herramienta;
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	private static String dockerSharedPath;
	
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		
		config = new Configuracion();
		registerPath = System.getProperty("user.dir") + config.getLogPath();
		csvPath = System.getProperty("user.dir") + config.getCsvPath();
		dockerSharedPath = System.getProperty("user.dir") + config.getDockerSharedDirectory();
		System.out.println(dockerSharedPath);
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
		
		// Comprobar que la carpeta compartida con el docker existe
		File dockerSharedDirectory = new File(dockerSharedPath);
		if (!dockerSharedDirectory.exists()) {
			// Si no existe se crea
			dockerSharedDirectory.mkdir();
		}
		herramienta = new Herramientas();

		String registerTransformedPath = System.getProperty("user.dir") + config.getLogPath()
		+ System.getProperty("file.separator") + "transformed.log";
		
		// Leer fichero de log de ficheros ya convertidos a CSV(transformed.log)
		File transformedLogFile = new File(registerTransformedPath);
		
		if(transformedLogFile.exists()) {
			ArrayList<JSONObject> lista = herramienta.obtenerEntradas(transformedLogFile);
			
			if(!lista.isEmpty()) {
				System.out.println(lista);
				for (int i = 0; i < lista.size(); i++) {
					// Obtener el objeto JSON
					JSONObject jsonObject = lista.get(i);
					// Realizar la insercion TODO: Habra que modificar para cuando hayan otros tipos descargados
					int result = insertarDatos(jsonObject);
//					// Si la insercion se desarrolla correctamente
					if(result==1) {
//					// Eliminar la linea del fichero
						herramienta.eliminarLineaFichero(transformedLogFile, lista.get(i));
					}
				}
				
			}
			
		}
		
	}
	
	private static int insertarDatos(JSONObject jsonObject) throws JsonProcessingException {
		String pathFicheroCSV = (String) jsonObject.get("PathCompleto");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		/*
		 * Copiar fichero a la carpeta compartida del docker, para poder realizar la
		 * insercion de los datos.
		 */
		// Nombre del fichero que se ha tratado
		String nombreFicheroCSV = pathFicheroCSV.substring(pathFicheroCSV.lastIndexOf(System.getProperty("file.separator")) + 1);
		Path origenPath = FileSystems.getDefault().getPath(pathFicheroCSV);
		Path destPath = FileSystems.getDefault().getPath(dockerSharedPath + System.getProperty("file.separator") + nombreFicheroCSV);
		System.out.println(destPath);
		try {
			Files.copy(origenPath, destPath,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
			return -1;
		}
		
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			return -1;
		}
		// Conectar con la base de datos
		try (Connection con = DriverManager.getConnection("jdbc:hive2://155.210.155.121:10000/default", "hive", "hive");) {
			
			Statement stmt = con.createStatement();

			String nombreTablaUsoEstacionesTmp = "USOESTACIONESTMP";
			// TODO: HABRA QUE TENER EN CUENTA QUE EN ESTE MOMENTO SE ESTA INSERTANDO EN
			// DEFAULT, HAY QUE CAMBIARLO
			String createTableUsoEstacionesTmp = "create table if not exists " + nombreTablaUsoEstacionesTmp
					+ "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso String,"
					+ "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
					+ "retiradasMedia float, neto float, total float, fechaObtencionDatos String, ficheroCSV  String,"
					+ "ficheroXLS  String) " + "row format delimited fields terminated by \",\" "
					+ "lines terminated BY \'\n\' " + "tblproperties(\"skip.header.line.count\"=\"1\")";

			stmt.execute(createTableUsoEstacionesTmp);

			// Generar Path que sea congruente con el sistema de ficheros del docker
			String pathFileInDocker = System.getProperty("file.separator") + dockerSharedPath.substring(dockerSharedPath.lastIndexOf(System.getProperty("file.separator"))+1);
			pathFileInDocker += System.getProperty("file.separator") +nombreFicheroCSV;
			
			// Carga de datos en la tabla auxiliar
			String loadDataCSV = "LOAD DATA LOCAL INPATH '" + pathFileInDocker + "' INTO TABLE "
					+ nombreTablaUsoEstacionesTmp;
			stmt.execute(loadDataCSV);
			
			// Trasladar los datos de la tabla anterior a una table con formato de almacenamiento ORC
			String nombreTablaUsoEstacionesORC = "USOESTACIONESORC";
			String createTableUsoEstacionesORC = "create table if not exists " + nombreTablaUsoEstacionesORC
					+ "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso String,"
					+ "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
					+ "retiradasMedia float, neto float, total float, fechaObtencionDatos String, ficheroCSV  String,"
					+ "ficheroXLS  String) STORED AS ORC";
			
			stmt.execute(createTableUsoEstacionesORC);
			
			/*
			 * Insertar solo registros que no esten contenidas.
			 * Evitar que se inserten registros ya procesados.
			 * Se usa el nombre del ficheroXL de donde provienen los datos originales,
			 * ya que sirve como un identificador unico.
			 */
			String selectControl = "select ficheroXLS from " + nombreTablaUsoEstacionesORC;
			String selectAinsertar = "select * from " + nombreTablaUsoEstacionesTmp + " tmp where tmp.ficheroXLS not in ";
			String insertDataORCsinRepetidos = "INSERT INTO TABLE " + nombreTablaUsoEstacionesORC + " " + selectAinsertar + "(" + selectControl + ")";  
			
			stmt.execute(insertDataORCsinRepetidos);
			
			// Eliminar la tabla temporal
			stmt.execute("drop table if exists " + nombreTablaUsoEstacionesTmp);
			con.close();
			
			String fechaFichero = (String) jsonObject.get("FechaFichero");
			
			// TODO: Eliminar fichero de la carpeta compartida
			
			herramienta.generarLog(timestamp, Estado.SUCCESStoHADOOP, fechaFichero, pathFicheroCSV, "Uso de las estaciones",
					"3.1-Usos de las estaciones", Tipo.USOESTACION);
			return 1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return -1;
		}

		// Realizar la insercion

	}

}
