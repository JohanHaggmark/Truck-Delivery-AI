
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Write {

	String filePath = "D:\\Java AI\\Excel\\bok.xlsx";
	Workbook workbook;
	FileInputStream fis;
	FileOutputStream fos;

	public void write() {
//	try
//	{
//		fis = new FileInputStream(filePath);
//
//		workbook = WorkbookFactory.create(fis);
//
//		org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
//
//		for (int i = 0; i <= 5; i++) {
//
//			Row row = sheet.getRow(i);
//
//			if (row == null)
//				row = sheet.createRow(i);
//
//			Cell cell = row.getCell(0);
//
//			if (cell == null)
//				cell = row.createCell(0, CellType.STRING);
//
//			cell.setCellValue("Keyword" + i);
//
//			cell = row.getCell(1);
//
//			if (cell == null)
//				cell = row.createCell(1, CellType.STRING);
//			cell.setCellValue("PASS" + i);
//
//			System.out.println("1");
//		}
//		fis.close();
//		fos = new FileOutputStream(filePath);
//		workbook.write(fos);
//		fos.close();
//	}catch(
//	Exception e)
//	{
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}finally
//	{
//		// close out/in streams
//	}

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Datatypes in Java");
		Object[][] datatypes = { { "Datatype", "Type", "Size(in bytes)" }, { "int", "Primitive", 2 },
				{ "float", "Primitive", 4 }, { "double", "Primitive", 8 }, { "char", "Primitive", 1 },
				{ "String", "Non-Primitive", "No fixed size" } };

		int rowNum = 0;
		System.out.println("Creating excel");

		for (Object[] datatype : datatypes) {
			Row row = sheet.createRow(rowNum++);
			int colNum = 0;
			for (Object field : datatype) {
				Cell cell = row.createCell(colNum++);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}

	public void read() {
		try {

			String filePath = "D:\\Java AI\\Excel\\bok.xlsx";
			FileInputStream excelFile = new FileInputStream(new File(filePath));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = (Sheet) workbook.getSheetAt(0);

			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();
					// getCellTypeEnum shown as deprecated for version 3.15
					// getCellTypeEnum ill be renamed to getCellType starting from version 4.0
					if (currentCell.getCellTypeEnum() == CellType.STRING) {
						System.out.print(currentCell.getStringCellValue() + "--");
					} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
						System.out.print(currentCell.getNumericCellValue() + "--");
					}

				}
				System.out.println();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void append(int row, int collumn) {

		try {
			String filePath = "D:\\Java AI\\Excel\\bok.xlsx";
			FileInputStream excelFile = new FileInputStream(new File(filePath));
			Workbook workbook = new XSSFWorkbook(excelFile);

			Sheet datatypeSheet = (Sheet) workbook.getSheetAt(0);

			Cell cell = null;
			// for(int i = 0; i < 10; i++) {
			 //cell = datatypeSheet.getRow(1).getCell(4);
			int rowNumber = getNumberOfRowsInColumn(collumn, datatypeSheet);
			cell = datatypeSheet.createRow(rowNumber).createCell(collumn);
			cell.setCellValue(Integer.toString(2231324));
			// }
			
			getNumberOfRowsInColumn(1, datatypeSheet);

			excelFile.close();

			FileOutputStream output_file = new FileOutputStream(new File(filePath));
			workbook.write(output_file);
			output_file.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getNumberOfRowsInColumn(int collumn, Sheet sheet) {

		int rowCount = 0;

		Iterator<Row> rowIter = sheet.rowIterator();
		Row r = (Row) rowIter.next();
		int lastCellNum = r.getLastCellNum();
		int[] dataCount = new int[100];
		int col = 0;
		rowIter = sheet.rowIterator();

		while (rowIter.hasNext()) {
			Iterator cellIter = ((Row) rowIter.next()).cellIterator();
			while (cellIter.hasNext()) {
				Cell cell = (Cell) cellIter.next();
				col = cell.getColumnIndex();
				dataCount[col] += 1;
			}
		}
		System.out.println(dataCount[collumn]);
		return dataCount[collumn];
	}
}
