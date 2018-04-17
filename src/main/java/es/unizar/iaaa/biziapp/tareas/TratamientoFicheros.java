package es.unizar.iaaa.biziapp.tareas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import es.unizar.iaaa.biziapp.domain.enumeration.Estado;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * VERSION JHIPSTER
 * @author dani
 *
 */
@Service
public class TratamientoFicheros {

    @Autowired
    private Configuracion configuracion;
    private static Herramientas herramienta;
    private static String csvPath;
    private static String pathCompletoCSV = null;
    private static final Logger log = LoggerFactory.getLogger(TratamientoFicheros.class);

    public void tratarFichero() {

        herramienta = new Herramientas();
        csvPath = configuracion.getCsvPath();

        // Comprobar que la carpeta donde se guardaran los CSV existe
        File csvDirectory = new File(csvPath);
        if (!csvDirectory.exists()) {
            // Si no existe se crea
            csvDirectory.mkdir();
        }

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

        // Conectar con base de datos(tareas)
        try (Connection con = DriverManager.getConnection(jdbcMysql, userMysql, passwordMysql)) {
            Statement stmtSelect = con.createStatement();
            Statement stmtUpdate = con.createStatement();
            Statement stmtInsert = con.createStatement();
            Statement stmtSelectInsercion = con.createStatement();
            String nombreTablaTratamiento = "tratamiento";

            // Obtener las ultimas X fechas introducidas en tareas.descargas
            String querySelect = "SELECT * FROM " + nombreTablaTratamiento + " WHERE estado='" + Estado.WAITING + "' ORDER BY id DESC LIMIT 1";
            ResultSet rs = stmtSelect.executeQuery(querySelect);

            // Marcarlas como en proceso en tareas.descargas
            while (rs.next()) {
                // estado=2 => 'PROCESING"
                stmtUpdate.execute("UPDATE " +nombreTablaTratamiento + " SET estado='" + Estado.PROCESING + "' where id=" + rs.getInt("id"));
                String pathFicheroXLS = rs.getString("path_fichero_xls");
                int idTarea = rs.getInt("id_tarea");

                int existe = herramienta.comprobarFichero(pathFicheroXLS);
                // Si el fichero XLS existe
                if (existe == 1) {
                    int result = tratarXLS(pathFicheroXLS);
                    // Si el fichero CSV se genera correctamente
                    if (result == 1 && pathCompletoCSV != null) {
                        // estado = 3 => 'FINISHED'
                        stmtUpdate.execute("UPDATE " + nombreTablaTratamiento + " SET estado='" + Estado.FINISHED + "' where id=" + rs.getInt("id"));

                        // Variables para insertar valores en tabla insertarHadoop
                        String nombreTablaInsercion = "insercion";
                        String tipo = "'" + rs.getString("tipo") + "'";
                        String fechaFichero = "'" + rs.getString("fecha_fichero") + "'";
                        String path = "'" + pathCompletoCSV + "'";

                        // Comprobar si ya existe la entrada en la tabla Insercion
                        querySelect = "SELECT * FROM " + nombreTablaInsercion + " where id=" + idTarea;
                        ResultSet rs2 = stmtSelectInsercion.executeQuery(querySelect);
                        // Si devuelve algo significa que la tupla ya existe y hay que modificarla en vez de crearla
                        if(rs2.next()) {
                            // Modificar el valor para ponerlo en espera de procesamiento
                            stmtUpdate.execute("UPDATE " + nombreTablaInsercion + " SET estado='" + Estado.WAITING + "' where id_tarea=" + idTarea);
                        } else {
                            // Realizar insercion de nueva tupla
                            String insert = "INSERT INTO " + nombreTablaInsercion +
                                " (id_tarea, tipo, fecha_fichero, path_fichero_csv, estado) " +
                                "VALUES (" + idTarea + ", " + tipo + "," + fechaFichero + "," + path + ",'" + Estado.WAITING +"');";
                            stmtInsert.execute(insert);
                            log.info("Generado fichero CSV: {}", path);
                            log.info("Entrada en tabla insercion. Fecha fichero: {}", fechaFichero);
                        }

                    } else {
                        // estado = 1 => 'WAITING'
                        stmtUpdate.execute("UPDATE " + nombreTablaTratamiento + " SET estado='" + Estado.WAITING + "' where id=" + rs.getInt("id"));
                    }
                } else { // Si el fichero XLS no existe
                    // Marcar como error la tupla de la tablas.generarCSV estado = 4 => 'ERROR'
                    stmtUpdate.execute("UPDATE " + nombreTablaTratamiento + " SET estado='" + Estado.ERROR + "' where id=" + rs.getInt("id"));
                    // Modificar el estado de la tupla en descargas, para que el fichero vuelva a ser descargado
                    String nombreTablaDescarga = "descarga";
                    stmtUpdate.execute("UPDATE " + nombreTablaDescarga + " SET estado='" + Estado.WAITING + "' where id=" + idTarea);
                }

            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tratamiento de los ficheros xls para convertirlos en csv. A parte del
     * contenido obtenido del xls, se le aniaden otros elementos para enriquecer el
     * fichero.
     *
     * @param pathFicheroXLS Ruta completa del fichero xls que va a ser tratado.
     * @return 1 = Operacion realizada correctamenete. -1 = Operacion fallida
     */
    private static int tratarXLS(String pathFicheroXLS) {
        try {
            FileInputStream excelFile = new FileInputStream(new File(pathFicheroXLS));

            HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
            HSSFSheet sheet = workbook.getSheetAt(0);

            // Obtencion de datos relevantes para el fichero CSV
            Map<String, ArrayList<String>> datos = extraerDatosExcel(sheet);
            String fechaUso = extraerFechaDeUso(sheet);
            String fechaExtraccion = extraerFechaDeExtraccion(sheet);

            excelFile.close();
            // Nombre del fichero que se ha tratado
            String nombreFicheroXLS = pathFicheroXLS
                .substring(pathFicheroXLS.lastIndexOf(System.getProperty("file.separator")) + 1);

            // Generar fichero CSV a partir de los datos obtenidos
            pathCompletoCSV = crearCSV(datos, fechaUso, fechaExtraccion, nombreFicheroXLS);

            return 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Obtiene los datos recogidos en un fichero excel con una estructura definida.
     * La estructura es la que viene dada por los ficheros descargados de la web clearchannel.
     *
     * @param sheet Hoja del fichero excel de la que se obtiene la informacion.
     * @return Mapa clave/valor de los datos extraidos del fichero.
     */
    private static Map<String, ArrayList<String>> extraerDatosExcel(HSSFSheet sheet) {

        Iterator<Row> iterator = sheet.iterator();
        String nombreEstacion = null;
        ArrayList<String> lista = null;
        Map<String, ArrayList<String>> datos = new HashMap<>();

        iteradores:
        while (iterator.hasNext()) {

            String datosFila = "";
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();

            // Recorrer cada celda de la fila.
            // Datos relevantes a partir de la fila 11 (Fila 12 en Excel)
            while (cellIterator.hasNext() && currentRow.getRowNum() >= 11) {

                HSSFCell currentCell = (HSSFCell) cellIterator.next();

                if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    // Si encuentra texto en la columna D ("Total Todos los
                    // horarios")
                    if (currentCell.getColumnIndex() == 3) {
                        // Salir de ambos bucles. Fin de datos relevantes en
                        // Excel
                        break iteradores;
                    } else {
                        // Si encuentra texto en la columna B se trata del
                        // nombre de la estacion
                        if (currentCell.getColumnIndex() == 1) {
                            nombreEstacion = currentCell.getStringCellValue().replaceAll(",", "").trim();
                            lista = new ArrayList<>();
                        } else {
                            datosFila = datosFila.concat(currentCell.getStringCellValue().replace(",", ".") + ",");
                        }
                    }
                }
            }
            // Si no se trata de una fila vacia, se insertan los valores al Map
            if (!datosFila.equals("")) {
                lista.add(datosFila);
                datos.put(nombreEstacion, lista);
            }
        }
        return datos;
    }

    /**
     * Obtiene la fecha que especifica de cuando es la informacion contenida en el fichero.
     *
     * @param sheet Hoja del fichero excel de la que se obtiene la informacion.
     * @return fecha en formato dd/MM/yyyy
     */
    private static String extraerFechaDeUso(HSSFSheet sheet) {
        CellReference cellReference = new CellReference("C9");
        HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
        HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
        String fechaDeUso = hssfcell.toString();
        String[] split = fechaDeUso.split(" ");
        fechaDeUso = split[split.length - 1];
        return fechaDeUso;
    }

    /**
     * Obtiene la fecha que especifica de cuando se realizo la descarga del fichero.
     *
     * @param sheet Hoja del fichero excel de la que se obtiene la informacion.
     * @return fecha en formato dd/MM/yyyy
     */
    private static String extraerFechaDeExtraccion(HSSFSheet sheet) {

        CellReference cellReference = new CellReference("L3");
        HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
        HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
        String fechaDeExtraccion = hssfcell.toString();
        return fechaDeExtraccion;
    }

    /**
     * Genera fichero CSV.
     *
     * @param datos            Mapa clava/valor con los datos extraidos del fichero xls.
     * @param fechaUso         Fecha que especifica de cuando es la informacion contenida en el
     *                         fichero. En formato dd/MM/yyyy
     * @param fechaExtraccion  Fecha que especifica de cuando se realizo la descarga del fichero.
     *                         En formato dd/MM/yyyy
     * @param nombreFicheroXLS Nombre que tiene el fichero xls de donde se obtuvieron los datos.
     */
    private static String crearCSV(Map<String, ArrayList<String>> datos, String fechaUso, String fechaExtraccion,
                                   String nombreFicheroXLS) {

        // Prueba de generacion de nombre con messageFormat
        String nombreFicheroCSV = MessageFormat.format("{0}_{1}.csv",
            nombreFicheroXLS.substring(0, nombreFicheroXLS.lastIndexOf(".")), fechaExtraccion.replace("/", ""));

        String ruta = csvPath + System.getProperty("file.separator") + nombreFicheroCSV;

        try {
            File archivo = new File(ruta);
            BufferedWriter bw;

            if (!archivo.exists()) {
                bw = new BufferedWriter(new FileWriter(archivo));

                // Introducir cabeceras
                bw.write("nombreCompleto,idEstacion,nombreEstacion," + "fechaDeUso,intervaloDeTiempo,devolucionTotal,"
                    + "devolucionMedia,retiradasTotal,retiradasMedia,"
                    + "neto,total,fechaObtencionDatos,ficheroCSV,ficheroXLS," + "hashCode\n");

                // Introducir datos
                for (Entry<String, ArrayList<String>> dato : datos.entrySet()) {
                    // Extraer el id de la estacion
                    String idEstacion = dato.getKey().split(" ")[0];
                    // Extraer el nombre de la estacion
                    String nombreEstacion = dato.getKey().substring(dato.getKey().indexOf("- ") + 1).trim();

                    // Cambiar formato de las fechas a YYYY-MM-DD
                    String[] fechaUsoSplit = fechaUso.split("/");
                    String nuevaFechaUso = MessageFormat.format("{0}-{1}-{2}", fechaUsoSplit[2], fechaUsoSplit[1],
                        fechaUsoSplit[0]);

                    String[] fechaExtraccionSplit = fechaExtraccion.split("/");
                    String nuevaFechaExtraccion = MessageFormat.format("{0}-{1}-{2}", fechaExtraccionSplit[2],
                        fechaExtraccionSplit[1], fechaExtraccionSplit[0]);

                    // Escribir en el fichero
                    for (int i = 0; i < dato.getValue().size(); i++) {
                        // Generar hash de la linea a insertar
                        String hash = herramienta.generarHash(dato.getKey() + "," + idEstacion + "," + nombreEstacion + "," + nuevaFechaUso + ","
                            + dato.getValue().get(i) + "," + nombreFicheroXLS);
                        // Insertar linea
                        bw.write(dato.getKey() + "," + idEstacion + "," + nombreEstacion + "," + nuevaFechaUso + ","
                            + dato.getValue().get(i) + nuevaFechaExtraccion + "," + nombreFicheroCSV + ","
                            + nombreFicheroXLS + "," + hash + "\n");
                    }
                }

                bw.close();
            }
            return ruta;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
