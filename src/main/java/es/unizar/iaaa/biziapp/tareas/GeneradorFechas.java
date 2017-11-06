package es.unizar.iaaa.biziapp.tareas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by dani on 29/10/17.
 *
 */
public class GeneradorFechas {

    private static Configuracion configuracion;
    private static final Logger log = LoggerFactory.getLogger(GeneradorFechas.class);

    public void generarFechasUsoEstacion() {

        configuracion = new Configuracion();
        String driverNameMysql = configuracion.getDriverNameMysqlDB();
        String jdbcMysql = configuracion.getJdbcMysqlConnector();
        String[] credentialMysql = configuracion.getCredentialMysqlDB().split(":");
        String userMysql = credentialMysql[0];
        String passwordMysql = credentialMysql[1];

        // Obtener fecha del dia anterior
        LocalDate date = LocalDate.now().plusDays(-1);
        // Dar formato de salida
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fecha = date.format(formatter);

        // Generar entrada para la base de datos
        int tipo = 1; //1 = USOESTACION
        String categoria = "'Uso de las estaciones'";
        String subcategoria = "'3.1-Usos de las estaciones'";
        String nombreTabla = "descargas";
        String sqlInsert = "INSERT INTO " + nombreTabla +
            " (tipo, fechaFichero, categoria, subcategoria) " +
            "VALUES (" + tipo + ",'" + fecha + "'," + categoria +
            "," + subcategoria + ");";

        //Conectar a la base de datos

        try {
            Class.forName(driverNameMysql).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection con = DriverManager.getConnection(
            jdbcMysql, userMysql, passwordMysql)) {

            Statement stmt = con.createStatement();

            //Realizar insercion
            stmt.execute(sqlInsert);
            con.close();
            log.info("Insertado con exito la tarea de descarga del fichero con fecha: {}", fecha);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
