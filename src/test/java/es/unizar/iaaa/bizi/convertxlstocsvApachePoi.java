package es.unizar.iaaa.bizi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

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

	private static final String EXCEL_FILE_LOCATION = "C:\\Users\\686013\\Desktop\\usoEstacion.xls";
	
	
	public static void main(String[] args) {
		
//		extraerDatosExcel();
		
		 try {
			 FileInputStream excelFile = new FileInputStream(new File(EXCEL_FILE_LOCATION));
			
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
			HSSFSheet sheet = workbook.getSheetAt(0);

//			CellReference cellReference = new CellReference("B12");
//			HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
//			HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
////			System.out.println(hssfcell.getCellStyle().getFillBackgroundColorColor());
////			System.out.println(hssfcell.getCellStyle().getFont(workbook));
//			System.out.println(hssfcell.getCellStyle().getFillForegroundColorColor());
			
			Iterator<Row> iterator = sheet.iterator();
			String nombre=null;
			
			iteradores:
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                
                Iterator<Cell> cellIterator = currentRow.iterator();
                boolean saltoDeLinea = false;

                if(nombre!=null){
                	System.out.print("row " + currentRow.getRowNum() + " ");
                	System.out.print(nombre + ",");
                }
                
                while (cellIterator.hasNext() && currentRow.getRowNum()>=11) {
                	
                	HSSFCell currentCell = (HSSFCell) cellIterator.next();
//                	System.out.print("currentCell.getRowIndex()+1: " + (currentCell.getRowIndex()+1) + " ");

                	if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    	
                    	if(currentCell.getStringCellValue().equals("Total Todos los horarios")){

                        	nombre = null;
                        	break iteradores;
                        }else{
                        	if(currentCell.getColumnIndex()==1){
                        		nombre = currentCell.getStringCellValue();
                        		System.out.print(nombre + ",");
                        	}else{
                            	System.out.print(currentCell.getStringCellValue().replace(",", ".") + ",");
                        	}
                        	saltoDeLinea=true;
                        	
                        }
                    }else if(currentCell.getAddress().getColumn()==5){
                    	nombre = null;
                    	break iteradores;
                    }
                    
                }
                if(saltoDeLinea==true){
                	System.out.println();                	
                }
                
                
            }
            excelFile.close();
			
		 } catch (FileNotFoundException e) {
	            e.printStackTrace();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }

	}
	
	private static void extraerDatosExcel(){
		
		String idEstacion, nombreEstacion, fechaDeUso, 
		IntervaloDeTiempo, devolucionTotal, devolucionMedia, 
		retiradasTotal, retiradasMedia, neto, total, 
		fechaObtencionDatos, ficheroCSV, ficheroXLS;
		
		try {
			FileInputStream excelFile = new FileInputStream(new File(EXCEL_FILE_LOCATION));
			
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			idEstacion = extraerIdEstacion(sheet);
			System.out.println(idEstacion);
			nombreEstacion = extraerNombreEstacion(sheet);
			System.out.println(nombreEstacion);
			fechaDeUso = extraerFechaDeUso(sheet);
			System.out.println(fechaDeUso);
			
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String extraerIdEstacion(HSSFSheet sheet){
		String idEstacion = null;
		CellReference cellReference = new CellReference("B12");
		HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
		HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
		String[] split = hssfcell.toString().split(" ");
		idEstacion = split[0];
		return idEstacion;
	}
	
	private static String extraerNombreEstacion(HSSFSheet sheet){
		String nombreEstacion = null;
		CellReference cellReference = new CellReference("B12");
		HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
		HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
		nombreEstacion = hssfcell.toString();
		nombreEstacion = nombreEstacion.substring(hssfcell.toString().indexOf("- ")+1);
		nombreEstacion = nombreEstacion.trim();
		return nombreEstacion;
	}
	
	private static String extraerFechaDeUso(HSSFSheet sheet){
		String fechaDeUso = null;
		CellReference cellReference = new CellReference("C9");
		HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
		HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
		fechaDeUso = hssfcell.toString();
		String[] split = fechaDeUso.split(" ");
		fechaDeUso = split[split.length-1];
		return fechaDeUso;
	}
	
	
	private void crearCSV(String datos){
		
	}

}
