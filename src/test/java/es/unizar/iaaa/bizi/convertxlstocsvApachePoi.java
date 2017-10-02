package es.unizar.iaaa.bizi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
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

public class convertxlstocsvApachePoi {

	// TODO: Modificar extraer id y nombre de estaci�n. Hacer genericos

	private static final String nombreFicheroXLS = "usoEstacion3.xls";
	private static final String EXCEL_FILE_LOCATION = "C:\\Users\\686013\\Desktop\\" + nombreFicheroXLS;
	private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault());

	public static void main(String[] args) {

		try {
			FileInputStream excelFile = new FileInputStream(new File(EXCEL_FILE_LOCATION));

			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Obtencion de datos relevantes para el fichero CSV
			Map<String, ArrayList<String>> datos = extraerDatosExcel(sheet);
			String fechaUso = extraerFechaDeUso(sheet);
			String fechaExtraccion = extraerFechaDeExtraccion(sheet);

			excelFile.close();

			crearCSV(datos, fechaUso, fechaExtraccion, nombreFicheroXLS);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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
							nombreEstacion = currentCell.getStringCellValue();
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

	private static String extraerFechaDeExtraccion(HSSFSheet sheet) {
		String fechaDeExtraccion = null;
		CellReference cellReference = new CellReference("L3");
		HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
		HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
		fechaDeExtraccion = hssfcell.toString();
		return fechaDeExtraccion;
	}

	private static void crearCSV(Map<String, ArrayList<String>> datos, String fechaUso, String fechaExtraccion,
			String nombreFicheroXLS) {

		// Prueba de generacion de nombre con messageFormat
		String nombreFicheroCSV = MessageFormat.format("{0}_{1}.csv", nombreFicheroXLS.substring(0, nombreFicheroXLS.indexOf(".")),
				fechaExtraccion.replace("/", ""));
		System.out.println(nombreFicheroCSV);

		String ruta = "C:\\Users\\686013\\Desktop\\FicherosCSV\\" + nombreFicheroCSV;
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

					for (int i = 0; i < dato.getValue().size(); i++) {
						bw.write(dato.getKey() + "," + idEstacion + "," + nombreEstacion + "," + fechaUso + ","
								+ dato.getValue().get(i) + fechaExtraccion + "," + nombreFicheroCSV + ","
								+ nombreFicheroXLS + "\n");
					}
				}

				bw.close();
				System.out.println("Generado fichero: " + nombreFicheroCSV);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
