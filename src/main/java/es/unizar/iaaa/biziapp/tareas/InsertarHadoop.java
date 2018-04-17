package es.unizar.iaaa.biziapp.tareas;

import es.unizar.iaaa.biziapp.domain.enumeration.Estado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

/**
 * Created by dani on 29/10/17.
 */
@Service
public class InsertarHadoop {

    @Autowired
    private Configuracion configuracion;
    private static String dockerSharedPath;
    private static final Logger log = LoggerFactory.getLogger(InsertarHadoop.class);

    public void insertarHadoop() {
        Herramientas herramienta = new Herramientas();
        String csvPath = configuracion.getCsvPath();
        dockerSharedPath = configuracion.getDockerSharedDirectory();

        // Configuración MySQL
        String jdbcMysql = configuracion.getJdbcMysqlConnector();
        String driverNameMysql = configuracion.getDriverNameMysqlDB();
        String[] credentialMysqlDB = configuracion.getCredentialMysqlDB().split(":");
        String userMysql = credentialMysqlDB[0];
        String passwordMysql = credentialMysqlDB[1];

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
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Conectar con base de datos(tareas)
        try (Connection con = DriverManager.getConnection(jdbcMysql, userMysql, passwordMysql)) {
            Statement stmtSelect = con.createStatement();
            Statement stmtUpdate = con.createStatement();
            String nombreTablaInsercion = "insercion";

            // Obtener las ultimas X fechas introducidas en tareas.descargas
            String querySelect = "SELECT * FROM " + nombreTablaInsercion + " WHERE estado='" + Estado.WAITING + "' ORDER BY id DESC LIMIT 1";
            ResultSet rs = stmtSelect.executeQuery(querySelect);

            // Marcarlas como en proceso en tareas.descargas
            while (rs.next()) {
                // estado=2 => 'PROCESING"
                stmtUpdate.execute("UPDATE " + nombreTablaInsercion + " SET estado='" + Estado.PROCESING + "' where id=" + rs.getInt("id"));
                String pathFicheroCSV = rs.getString("path_fichero_csv");
                int idTarea = rs.getInt("id_tarea");

                int existe = herramienta.comprobarFichero(pathFicheroCSV);
                // Si el fichero CSV existe
                if (existe == 1) {
                    // Realizar insercion
                    int result = insertarDatos(pathFicheroCSV);
                    // Si se inserta correctamente
                    if (result == 1) {
                        // estado = 3 => 'FINISHED'
                        stmtUpdate.execute("UPDATE " + nombreTablaInsercion + " SET estado='" + Estado.FINISHED + "' where id=" + rs.getInt("id"));
                        log.info("Fichero insertado en Hadoop: {}", pathFicheroCSV);

                    } else {
                        // estado = 1 => 'WAITING'
                        stmtUpdate.execute("UPDATE " + nombreTablaInsercion + " SET estado='" + Estado.WAITING + "' where id=" + rs.getInt("id"));
                    }
                } else { // Si el fichero CSV no existe
                    // Marcar como error la tupla de la insercion estado = 4 => 'ERROR'
                    stmtUpdate.execute("UPDATE " + nombreTablaInsercion + " SET estado='" + Estado.ERROR + "' where id=" + rs.getInt("id"));
                    // Modificar el estado de la tupla en tratamiento, para que se vuelva a generar el fichero CSV
                    String nombreTablaTratamiento = "tratamiento";
                    stmtUpdate.execute("UPDATE " + nombreTablaTratamiento + " SET estado='" + Estado.WAITING + "' where id_tarea=" + idTarea);
                }

            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private int insertarDatos(String pathCompletoCSV) {
		/*
		 * Copiar fichero a la carpeta compartida del docker, para poder realizar la
		 * insercion de los datos.
		 */
        // Nombre del fichero que se ha tratado
        String nombreFicheroCSV = pathCompletoCSV.substring(pathCompletoCSV.lastIndexOf(System.getProperty("file.separator")) + 1);
        Path origenPath = FileSystems.getDefault().getPath(pathCompletoCSV);
        Path destPath = FileSystems.getDefault().getPath(dockerSharedPath + System.getProperty("file.separator") + nombreFicheroCSV);

        try {
            Files.copy(origenPath, destPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e1) {
            e1.printStackTrace();
            return -1;
        }

        // Configuracion Hive
        String jdbcHive = configuracion.getJdbcHiveConnector();
        String driverNameHive = configuracion.getDriverNameHiveDB();
        String[] credentialHiveDB = configuracion.getCredentialHiveDB().split(":");
        String userHive = credentialHiveDB[0];
        String passwordHive = credentialHiveDB[1];


        try {
            Class.forName(driverNameHive);
        } catch (ClassNotFoundException e) {
            // Eliminar fichero de la carpeta compartida del docker
            File fileToDelete = new File(destPath.toString());
            fileToDelete.delete();
            e.printStackTrace();
            return -1;
        }

        // Conectar con la base de datos
        try (Connection conHive = DriverManager.getConnection(jdbcHive, userHive, passwordHive)) {

            Statement stmt = conHive.createStatement();

            // Crear base de datos si no existe
            String nombreDB = "biziapp";
            String createBD = "create database if not exists " + nombreDB;
            stmt.execute(createBD);

            // Crear tabla para la carga de los nuevos datos.
            String nombreTablaUsoEstacionesTmp = "BIZIAPP.USOESTACIONESTMP";
            // Eliminar la tabla temporal si exite.
            stmt.execute("drop table if exists " + nombreTablaUsoEstacionesTmp);

            String createTableUsoEstacionesTmp = "create table if not exists " + nombreTablaUsoEstacionesTmp
                + "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso DATE,"
                + "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
                + "retiradasMedia float, neto float, total float, fechaObtencionDatos DATE, ficheroCSV  String,"
                + "ficheroXLS  String, hashCode String) " + "row format delimited fields terminated by \",\" "
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
            String nombreTablaUsoEstaciones = "BIZIAPP.USOESTACIONES";
            String createTableUsoEstaciones = "create table if not exists " + nombreTablaUsoEstaciones
                + "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso DATE,"
                + "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
                + "retiradasMedia float, neto float, total float, fechaObtencionDatos DATE, ficheroCSV  String,"
                + "ficheroXLS  String, hashCode String) " + "row format delimited fields terminated by \",\" "
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
