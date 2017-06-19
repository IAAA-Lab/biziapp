package es.unizar.iaaa.bizi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

public class convertxlstocsvApachePoi {
	
	// TODO: Modificar extraer id y nombre de estación. Hacer genericos

	private static final String nombreFicheroXLS = "usoEstacion3.xls";
	private static final String EXCEL_FILE_LOCATION = "C:\\Users\\686013\\Desktop\\" + nombreFicheroXLS;
	private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault());

	public static void main(String[] args) {

		try {
			FileInputStream excelFile = new FileInputStream(new File(EXCEL_FILE_LOCATION));

			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
			HSSFSheet sheet = workbook.getSheetAt(0);

			Map<String, ArrayList<String>> datos = extraerDatosExcel(sheet);
			// datos.forEach((k,v) -> System.out.println("Key: " + k + ": Value:
			// " + v));

			String idEstacion = extraerIdEstacion(sheet);
			// System.out.println(idEstacion);

			String nombreEstacion = extraerNombreEstacion(sheet);
			// System.out.println(nombreEstacion);

			String fechaUso = extraerFechaDeUso(sheet);
			// System.out.println(fechaUso);

			String fechaExtraccion = extraerFechaDeExtraccion(sheet);
			// System.out.println(fechaExtraccion);

			excelFile.close();

			// Generar nombre del fichero a partir de la fecha y el nombre del
			// fichero XLS original
			Date now = new Date();
			String nombreFichero = FORMATO_FECHA.format(now);
			String fichero = nombreFicheroXLS.substring(0, nombreFicheroXLS.indexOf("."));
			System.out.println(fichero);
			String nombreFicheroCSV = nombreFichero + "-" + fichero + ".csv";

			crearCSV(datos, idEstacion, nombreEstacion, fechaUso, fechaExtraccion, nombreFicheroCSV, nombreFicheroXLS);

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

	private static String extraerIdEstacion(HSSFSheet sheet) {
		String idEstacion = null;
		CellReference cellReference = new CellReference("B12");
		HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
		HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
		String[] split = hssfcell.toString().split(" ");
		idEstacion = split[0];
		return idEstacion;
	}

	private static String extraerNombreEstacion(HSSFSheet sheet) {
		String nombreEstacion = null;
		CellReference cellReference = new CellReference("B12");
		HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
		HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
		nombreEstacion = hssfcell.toString();
		nombreEstacion = nombreEstacion.substring(hssfcell.toString().indexOf("- ") + 1);
		nombreEstacion = nombreEstacion.trim();
		return nombreEstacion;
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

	private static void crearCSV(Map<String, ArrayList<String>> datos, String idEstacion, String nombreEstacion,
			String fechaUso, String fechaExtraccion, String nombreFicheroCSV, String nombreFicheroXLS) {

		String ruta = "C:\\Users\\686013\\Desktop\\FicherosCSV\\" + nombreFicheroCSV;
		try {
			File archivo = new File(ruta);
			BufferedWriter bw;

			if (!archivo.exists()) {
				bw = new BufferedWriter(new FileWriter(archivo));
				System.out.println("Generando fichero: " + nombreFicheroCSV);

				// Introducir cabeceras
				bw.write("nombreCompleto,idEstacion,nombreEstacion," + "fechaDeUso,IntervaloDeTiempo,devolucionTotal,"
						+ "devolucionMedia,retiradasTotal,retiradasMedia,"
						+ "neto(d-r),total,fechaObtencionDatos,ficheroCSV," + "ficheroXLS\n");

				// Introducir datos
				for (Entry<String, ArrayList<String>> dato : datos.entrySet()) {
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
