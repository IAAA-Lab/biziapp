/**
 * Clase para el tratamiento de los ficheros .xls y su conversión a .csv.
 * El fichero de entrada es una hoja de cálculo que contiene la información
 * extraida de la web clearchannel.
 * El fichero de salida es un CSV a partir de los datos del fichero anterior. 
 */
package es.unizar.iaaa.bizi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class TratamientoXLStoCSV {
	
	private static Configuracion config;
	private static String registerPath;
	private static String csvPath;
	private static Herramientas herramienta;
	
	public static void main(String[] args) throws IOException, ParseException {

		config = new Configuracion();
		registerPath = System.getProperty("user.dir") + config.getLogPath();
		csvPath = System.getProperty("user.dir") + config.getCsvPath();
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
		herramienta = new Herramientas();

		String registerDownloadPath = System.getProperty("user.dir") + config.getLogPath()
				+ System.getProperty("file.separator") + "download.log";

		// Lectura del fichero log en busca de alguna descarga fallida anterior
		File downloadLogFile = new File(registerDownloadPath);

		// Leer fichero download.log y obtener lista de ficheros a tratar
		if (downloadLogFile.exists()) {
			ArrayList<JSONObject> lista = herramienta.obtenerEntradas(downloadLogFile);

			// Si existen fichero a tratar:
			if (!lista.isEmpty()) {
				// Recorrer lista
				for (int i = 0; i < lista.size(); i++) {
					// Obtener el objeto JSON
					JSONObject jsonObject = lista.get(i);
					// Extraer la fecha del fichero que se va a descargar
					String pathFicheroXLS = (String) jsonObject.get("PathCompleto");
					System.out.println(pathFicheroXLS);
					// Realizar el tratamiento TODO: Habra que modificar para cuando hayan otros tipos descargados
					int result = tratarXLS(pathFicheroXLS);
					// Si el tratamiento se desarrolla correctamente
					if(result==1) {
					// Eliminar la linea del fichero
						herramienta.eliminarLineaFichero(downloadLogFile, lista.get(i));
					}
				}
			}
		}

	}

	/**
	 * Tratamiento de los ficheros xls para convertirlos en csv. A parte del
	 * contenido obtenido del xls, se le aniaden otros elementos para enriquecer el
	 * fichero.
	 * 
	 * @param pathFicheroXLS
	 *            Ruta completa del fichero xls que va a ser tratado.
	 * @param tipoDatosFichero
	 *            Tipo de fichero correspondiente a su contenido.
	 * @return
	 */
	private static int tratarXLS(String pathFicheroXLS) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
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
			String pathCompleto = crearCSV(datos, fechaUso, fechaExtraccion, nombreFicheroXLS);

			// Generar entrada en transformed.log
			herramienta.generarLog(timestamp, Estado.SUCCESStoCSV, fechaUso, pathCompleto, "Uso de las estaciones",
					"3.1-Usos de las estaciones", Tipo.USOESTACION);

			return 1;
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			return -1;
		} catch (IOException e) {
			// e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Obtiene los datos recogidos en un fichero excel con una estructura definida.
	 * La estructura es la que viene dada por los ficheros descargados de la web clearchannel.
	 * @param sheet Hoja del fichero excel de la que se obtiene la informacion.
	 * @return Mapa clave/valor de los datos extraidos del fichero.
	 */
	private static Map<String, ArrayList<String>> extraerDatosExcel(HSSFSheet sheet) {

		Iterator<Row> iterator = sheet.iterator();
		String nombreEstacion = null;
		ArrayList<String> lista = null;
		Map<String, ArrayList<String>> datos = new HashMap<>();

		iteradores: while (iterator.hasNext()) {

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
	 * @param sheet Hoja del fichero excel de la que se obtiene la informacion.
	 * @return fecha en formato dd/MM/yyyy
	 */
	private static String extraerFechaDeUso(HSSFSheet sheet) {
		String fechaDeUso = null;
		CellReference cellReference = new CellReference("C9");
		HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
		HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
		fechaDeUso = hssfcell.toString();
		String[] split = fechaDeUso.split(" ");
		fechaDeUso = split[split.length - 1];
		return fechaDeUso;
	}

	/**
	 * Obtiene la fecha que especifica de cuando se realizo la descarga del fichero.
	 * @param sheet Hoja del fichero excel de la que se obtiene la informacion.
	 * @return fecha en formato dd/MM/yyyy
	 */
	private static String extraerFechaDeExtraccion(HSSFSheet sheet) {
		String fechaDeExtraccion = null;
		CellReference cellReference = new CellReference("L3");
		HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
		HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
		fechaDeExtraccion = hssfcell.toString();
		return fechaDeExtraccion;
	}

	/**
	 * Genera fichero CSV.
	 * 
	 * @param datos
	 *            Mapa clava/valor con los datos extraidos del fichero xls.
	 * @param fechaUso
	 *            Fecha que especifica de cuando es la informacion contenida en el
	 *            fichero. En formato dd/MM/yyyy
	 * @param fechaExtraccion
	 *            Fecha que especifica de cuando se realizo la descarga del fichero.
	 *            En formato dd/MM/yyyy
	 * @param nombreFicheroXLS
	 *            Nombre que tiene el fichero xls de donde se obtuvieron los datos.
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
				System.out.println("Generando fichero: " + nombreFicheroCSV);

				// Introducir cabeceras
				bw.write("nombreCompleto,idEstacion,nombreEstacion," + "fechaDeUso,intervaloDeTiempo,devolucionTotal,"
						+ "devolucionMedia,retiradasTotal,retiradasMedia,"
						+ "neto,total,fechaObtencionDatos,ficheroCSV," + "ficheroXLS\n");

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

					for (int i = 0; i < dato.getValue().size(); i++) {
						bw.write(dato.getKey() + "," + idEstacion + "," + nombreEstacion + "," + nuevaFechaUso + ","
								+ dato.getValue().get(i) + nuevaFechaExtraccion + "," + nombreFicheroCSV + ","
								+ nombreFicheroXLS + "\n");
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
