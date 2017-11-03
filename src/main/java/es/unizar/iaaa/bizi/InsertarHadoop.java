package es.unizar.iaaa.bizi;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.parser.ParseException;


/**
 * VERSION JHIPSTER
 * @author dani
 *
 */
public class InsertarHadoop {
	
	private static Configuracion config;
	private static String csvPath;
	private static Herramientas herramienta;
	private static String driverNameHive, jdbcHive, userHive, passwordHive, dockerSharedPath;
	private static String driverNameMysql, jdbcMysql, userMysql, passwordMysql;

	public static void main(String[] args) throws IOException, ParseException {
		herramienta = new Herramientas();
		config = new Configuracion();
		csvPath = config.getCsvPath();
		dockerSharedPath = config.getDockerSharedDirectory();
		
		// Configuración MySQL
		jdbcMysql = config.getJdbcMysqlConnector();
		driverNameMysql = config.getDriverNameMysqlDB();
		String[] credentialMysqlDB = config.getCredentialMysqlDB().split(":");
		userMysql = credentialMysqlDB[0];
		passwordMysql = credentialMysqlDB[1];
		
		// Configuracion Hive
		jdbcHive = config.getJdbcHiveConnector();
		driverNameHive = config.getDriverNameHiveDB();
		String[] credentialHiveDB = config.getCredentialHiveDB().split(":");
		userHive = credentialHiveDB[0];
		passwordHive = credentialHiveDB[1];
		
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
		
		// Conectar a la base de datos

        try {
            Class.forName(driverNameMysql).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Conectar con base de datos(tareas)
        try (Connection con = DriverManager.getConnection(jdbcMysql, userMysql, passwordMysql);) {
            Statement stmtSelect = con.createStatement();
            Statement stmtUpdate = con.createStatement();

            // Obtener las ultimas X fechas introducidas en tareas.descargas
            String querySelect = "SELECT * FROM insertarHadoop WHERE estado=1 ORDER BY id DESC LIMIT 1";
            ResultSet rs = stmtSelect.executeQuery(querySelect);

            // Marcarlas como en proceso en tareas.descargas
            while (rs.next()) {
                // estado=2 => 'PROCESING"
                stmtUpdate.execute("UPDATE insertarHadoop SET estado=2 where id=" + rs.getInt("id"));
                String pathFicheroCSV = rs.getString("pathFicheroCSV");
                
                int existe = herramienta.comprobarFichero(pathFicheroCSV);
                // Si el fichero CSV existe
                if (existe == 1) {
                	// Realizar insercion
                    int result = insertarDatos(pathFicheroCSV);
                    // Si se inserta correctamente
                    if (result == 1) {
                        // estado = 3 => 'FINISHED'
                        stmtUpdate.execute("UPDATE insertarHadoop SET estado=3 where id=" + rs.getInt("id"));
//                        log.info("Fichero insertado en Hadoop: {}", path);

                    } else {
                        // estado = 1 => 'WAITING'
                        stmtUpdate.execute("UPDATE insertarHadoop SET estado=1 where id=" + rs.getInt("id"));
                    }
                } else { // Si el fichero CSV no existe
                    // Marcar como error la tupla de la tablas.insertarHadoop estado = 4 => 'ERROR'
                    stmtUpdate.execute("UPDATE insertarHadoop SET estado=4 where id=" + rs.getInt("id"));
                    // Modificar el estado de la tupla en generarCSV, para que se vuelva a generar el fichero CSV
                    stmtUpdate.execute("UPDATE generarCSV SET estado=1 where id=" + rs.getInt("id"));
                }

            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	private static int insertarDatos(String pathCompletoCSV) {
		/*
		 * Copiar fichero a la carpeta compartida del docker, para poder realizar la
		 * insercion de los datos.
		 */
		// Nombre del fichero que se ha tratado
		String nombreFicheroCSV = pathCompletoCSV.substring(pathCompletoCSV.lastIndexOf(System.getProperty("file.separator")) + 1);
		Path origenPath = FileSystems.getDefault().getPath(pathCompletoCSV);
		Path destPath = FileSystems.getDefault().getPath(dockerSharedPath + System.getProperty("file.separator") + nombreFicheroCSV);
		System.out.println(destPath);
		try {
			Files.copy(origenPath, destPath,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			e1.printStackTrace();
			return -1;
		}
		
		
		try {
			Class.forName(driverNameHive).newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// Eliminar fichero de la carpeta compartida del docker
			File fileToDelete = new File(destPath.toString());
			fileToDelete.delete();
			e.printStackTrace();
			return -1;
		}
		
		// Conectar con la base de datos
		try (Connection conHive = DriverManager.getConnection(jdbcHive, userHive, passwordHive);) {
		
			Statement stmt = conHive.createStatement();

			// Crear base de datos si no existe
			String createBD = "CREATE DATABASE IF NOT EXISTS BIZIAPP";
			stmt.execute(createBD);
			
			// Crear tabla para la carga de los nuevos datos.
			String nombreTablaUsoEstacionesTmp = "BIZIAPP.USOESTACIONESTMP";
			// Eliminar la tabla temporal si exite.
			stmt.execute("drop table if exists " + nombreTablaUsoEstacionesTmp);
			
			String createTableUsoEstacionesTmp = "create table if not exists " + nombreTablaUsoEstacionesTmp
					+ "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso DATE,"
					+ "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
					+ "retiradasMedia float, neto float, total float, fechaObtencionDatos DATE, ficheroCSV  String,"
					+ "ficheroXLS  String) " + "row format delimited fields terminated by \",\" "
					+ "lines terminated BY \'\n\' " + "tblproperties(\"skip.header.line.count\"=\"1\")";

			stmt.execute(createTableUsoEstacionesTmp);

			// Generar Path que sea congruente con el sistema de ficheros del docker
			String pathFileInDocker = System.getProperty("file.separator") + dockerSharedPath.substring(dockerSharedPath.lastIndexOf(System.getProperty("file.separator"))+1);
			pathFileInDocker += System.getProperty("file.separator") +nombreFicheroCSV;
			System.out.println(pathFileInDocker);
			
			// Carga de datos en la tabla auxiliar
			String loadDataCSV = "LOAD DATA LOCAL INPATH '" + pathFileInDocker + "' INTO TABLE "
					+ nombreTablaUsoEstacionesTmp;
			stmt.execute(loadDataCSV);
			/*
			 * IMPORTANTE: NO ES NECESARIO EL PASO DE CONVERTIR A ORC.
			 * 			HAY QUE ELIMINAR LOS PASOS SIGUIENTES
			 * 			PERO HAY QUE TENER CUIDADO CON EL TEMA DE INSERTAR ENTRADAS REPETIDAS
			 */
			// Trasladar los datos de la tabla anterior a una table con formato de almacenamiento ORC
			String nombreTablaUsoEstaciones = "BIZIAPP.USOESTACIONES";
			String createTableUsoEstaciones = "create table if not exists " + nombreTablaUsoEstaciones
					+ "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso DATE,"
					+ "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
					+ "retiradasMedia float, neto float, total float, fechaObtencionDatos DATE, ficheroCSV  String,"
					+ "ficheroXLS  String)" + "row format delimited fields terminated by \",\" "
					+ "lines terminated BY \'\n\' ";
			
			stmt.execute(createTableUsoEstaciones);
			/*
			 * Insertar solo registros que no esten contenidas.
			 * Evitar que se inserten registros ya procesados.
			 * Se usa el nombre del ficheroXL de donde provienen los datos originales,
			 * ya que sirve como un identificador unico.
			 */
			String selectControl = "select ficheroXLS from " + nombreTablaUsoEstaciones;
			String selectAinsertar = "select * from " + nombreTablaUsoEstacionesTmp + " tmp where tmp.ficheroXLS not in ";
			String insertDatasinRepetidos = "INSERT INTO TABLE " + nombreTablaUsoEstaciones + " " + selectAinsertar + "(" + selectControl + ")";  
			stmt.execute(insertDatasinRepetidos);
			
			// Eliminar la tabla temporal
			stmt.execute("drop table if exists " + nombreTablaUsoEstacionesTmp);
			conHive.close();
			
			
			// Eliminar fichero de la carpeta compartida del docker
			File fileToDelete = new File(destPath.toString());
			fileToDelete.delete();
			
			return 1;
			
		} catch (SQLException e) {
			// Eliminar fichero de la carpeta compartida del docker
			File fileToDelete = new File(destPath.toString());
			fileToDelete.delete();
			e.printStackTrace();
			return -1;
		}

	}
	
	
}
