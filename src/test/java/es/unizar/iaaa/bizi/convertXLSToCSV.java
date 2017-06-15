//package es.unizar.iaaa.bizi;
//
//import java.io.File;
//import java.io.IOException;
//
//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.read.biff.BiffException;
//
//public class convertXLSToCSV {
//	
////	private static final String EXCEL_FILE_LOCATION = "C:\\Users\\686013\\Desktop\\usoEstacion.xls";
//	
//	public static void main(String[] args) {
//
//		String EXCEL_FILE_LOCATION = "C:\\Users\\686013\\Desktop\\usoEstacion.xls";
//		Workbook workbook = null;
//		try{
//			workbook = Workbook.getWorkbook(new File(EXCEL_FILE_LOCATION));
//			
////			"3.1-Usos de las estaciones"
//			Sheet sheet = workbook.getSheet(0);
//			
//			Cell celda = sheet.getCell("B12");
//			Cell celda2 = sheet.getCell(1, 11);
//            System.out.println(celda.getContents());
//            System.out.println(celda2.getContents());
//			
//		} catch (IOException e) {
//            e.printStackTrace();
//        } catch (BiffException e) {
//            e.printStackTrace();
//        } finally {
//
//            if (workbook != null) {
//            	workbook.close();
//            }
//
//        }
//		
//	}
//
//}
