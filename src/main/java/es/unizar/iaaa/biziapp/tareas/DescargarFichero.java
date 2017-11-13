package es.unizar.iaaa.biziapp.tareas;
import es.unizar.iaaa.biziapp.domain.enumeration.Estado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * VERSION JHIPSTER
 * @author dani
 *
 */
public class DescargarFichero {

    private static Configuracion configuracion;
    private static String downloadPath;
    private static String pathFichero = null;
    private static Herramientas herramienta;
    private static final Logger log = LoggerFactory.getLogger(DescargarFichero.class);

    public static void descargarFichero() {

        configuracion = new Configuracion();
        herramienta = new Herramientas();
        downloadPath = configuracion.getDownloadPath();
        String driverNameMysql = configuracion.getDriverNameMysqlDB();
        String jdbcMysql = configuracion.getJdbcMysqlConnector();
        String[] credentialMysql = configuracion.getCredentialMysqlDB().split(":");
        String userMysql = credentialMysql[0];
        String passwordMysql = credentialMysql[1];

        // Conectar a la base de datos
        try {
            Class.forName(driverNameMysql).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection(jdbcMysql, userMysql, passwordMysql)) {
            Statement stmtSelectDescargas = con.createStatement();
            Statement stmtUpdate = con.createStatement();
            Statement stmtInsert = con.createStatement();
            Statement stmtSelectTratamiento = con.createStatement();

            String nombreTablaDescarga = "descarga";
            // Obtener las ultimas X fechas introducidas en tareas.descargas
            String querySelect = "SELECT * FROM " + nombreTablaDescarga + " WHERE estado='" + Estado.WAITING +"' ORDER BY id DESC LIMIT 5";
            ResultSet rs = stmtSelectDescargas.executeQuery(querySelect);

            // Marcarlas como en proceso en tareas.descargas
            while (rs.next()) {
                // estado = 2 => 'PROCESING'
                stmtUpdate.execute("UPDATE " + nombreTablaDescarga + " SET estado='" + Estado.PROCESING + "' where id=" + rs.getInt("id"));
                // Lanzar proceso de descarga
                int result = descargar(rs.getInt("id"), rs.getString("tipo"), rs.getString("fecha_fichero"),
                    rs.getString("categoria"), rs.getString("subcategoria"));
                if(result==1) {
                    // estado = 3 => 'FINISHED'
                    stmtUpdate.execute("UPDATE " + nombreTablaDescarga + " SET estado='" + Estado.FINISHED + "' where id=" + rs.getInt("id"));

                    // Variables para insertar valores en tabla tratamiento
                    String nombreTablaTratamiento = "tratamiento";
                    int idTarea = rs.getInt("id");
                    String tipo = "'" + rs.getString("tipo") + "'";
                    String fechaFichero = "'" + rs.getString("fecha_fichero") + "'";
                    String path = "'" + pathFichero +"'";

                    // Comprobar si ya existe la entrada en la tabla Tratamiento
                    querySelect = "SELECT * FROM " + nombreTablaTratamiento + " where id=" + idTarea;
                    ResultSet rs2 = stmtSelectTratamiento.executeQuery(querySelect);
                    // Si devuelve algo significa que la tupla ya existe y hay que modificarla en vez de crearla
                    if(rs2.next()) {
                        // Modificar el valor para ponerlo en espera de procesamiento
                        stmtUpdate.execute("UPDATE " + nombreTablaTratamiento + " SET estado='" + Estado.WAITING + "' where id_tarea=" + idTarea);
                    } else {
                        // Realizar insercion de nueva tupla
                        String insert = "INSERT INTO " + nombreTablaTratamiento +
                            " (id_tarea, tipo, fecha_fichero, path_fichero_xls, estado) " +
                            "VALUES (" + idTarea + ", " + tipo + "," + fechaFichero + "," + path + ",'" + Estado.WAITING +"');";
                        stmtInsert.execute(insert);
                        log.info("Entrada en tratamiento. Fecha fichero: {}", fechaFichero);
                    }
                // En caso de que el proceso no se complete correctamente
                }else if(result==-1) {
                    // estado = 1 => 'WAITING'
                    stmtUpdate.execute("UPDATE " + nombreTablaDescarga + " SET estado='" + Estado.WAITING + "' where id=" + rs.getInt("id"));
                }
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static int descargar(int id, String tipo, String fechaFichero, String categoria, String subcategoria) {

        UsoEstaciones usoEstacion = new UsoEstaciones();
        try {
            int result = usoEstacion.descargar(fechaFichero);
            // Renombrar el fichero descargado e intentar acceder a el para ver que existe
            if(result==1) {
                pathFichero = herramienta.renameFile(downloadPath, "3.1-Usos de las estaciones.xls", fechaFichero);
                FileReader archivo = new FileReader(pathFichero);
                archivo.read();
                archivo.close();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

}
