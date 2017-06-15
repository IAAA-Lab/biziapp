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
		
		 try {
			FileInputStream excelFile = new FileInputStream(new File(EXCEL_FILE_LOCATION));
			
//			Workbook workbook = new HSSFWorkbook(excelFile);
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
//			Sheet sheet = workbook.getSheetAt(0);
			HSSFSheet sheet = workbook.getSheetAt(0);

//			CellReference cellReference = new CellReference("B12");
//			HSSFRow hssfrow = sheet.getRow(cellReference.getRow());
//			HSSFCell hssfcell = hssfrow.getCell(cellReference.getCol());
////			System.out.println(hssfcell.getCellStyle().getFillBackgroundColorColor());
////			System.out.println(hssfcell.getCellStyle().getFont(workbook));
//			System.out.println(hssfcell.getCellStyle().getFillForegroundColorColor());
			
			
			Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();

                while (cellIterator.hasNext()) {

                	HSSFCell currentCell = (HSSFCell) cellIterator.next();
                    //getCellTypeEnum shown as deprecated for version 3.15
                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        System.out.print(currentCell.getStringCellValue().replace(",", ".") + 
                        		"(font size: " + currentCell.getCellStyle().getFont(workbook).getFontHeightInPoints() +")" + ",");
                        
                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        System.out.print(currentCell.getNumericCellValue() + ",");
                    }

                }
                System.out.println();
            }
            excelFile.close();
			
		 } catch (FileNotFoundException e) {
	            e.printStackTrace();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		

	}

}
