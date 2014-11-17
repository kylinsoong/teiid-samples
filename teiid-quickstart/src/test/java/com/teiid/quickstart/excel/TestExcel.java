package com.teiid.quickstart.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class TestExcel {
	
	public void process() throws Exception {
		File xlsFile = new File("src/file/otherholdings.xls");
		FileInputStream xlsFileStream = new FileInputStream(xlsFile);
		try {
			Workbook workbook = new HSSFWorkbook(xlsFileStream);
			Sheet sheet = workbook.getSheet("Sheet1");
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			Iterator<Row> rowIter = sheet.iterator();
			while(rowIter.hasNext()) {
				Row row = rowIter.next();
				System.out.print(row.getCell(0, Row.RETURN_BLANK_AS_NULL) + ", ");
				System.out.print(row.getCell(1, Row.RETURN_BLANK_AS_NULL) + ", ");
				System.out.println(row.getCell(2, Row.RETURN_BLANK_AS_NULL));
			}
			
			int firstRowNumber = sheet.getFirstRowNum();
			System.out.println(firstRowNumber);
		} finally {
			xlsFileStream.close();
		}
		
		
	}

	public static void main(String[] args) throws Exception {
		TestExcel test = new TestExcel();
		test.process();
	}

}
