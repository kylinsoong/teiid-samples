package com.teiid.quickstart.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class EventExample implements HSSFListener {
	
	public static void main(String[] args) throws IOException {
		File xlsFile = new File("src/file/otherholdings.xls");
		FileInputStream xlsFileStream = new FileInputStream(xlsFile);
		POIFSFileSystem poifs = new POIFSFileSystem(xlsFileStream);
	}
	
	private SSTRecord sstrec;


	@Override
	public void processRecord(Record record) {

		switch (record.getSid()) {
		case BOFRecord.sid :
			BOFRecord bof = (BOFRecord) record;
			if (bof.getType() == bof.TYPE_WORKBOOK) {
				System.out.println("Encountered workbook");
				// assigned to the class level member
			} else if (bof.getType() == bof.TYPE_WORKSHEET) {
				System.out.println("Encountered sheet reference");
			}
			break;
		case BoundSheetRecord.sid:
			BoundSheetRecord bsr = (BoundSheetRecord) record;
            System.out.println("New sheet named: " + bsr.getSheetname());
            break;
		case RowRecord.sid:
			RowRecord rowrec = (RowRecord) record;
			System.out.println("Row found, first column at "  + rowrec.getFirstCol() + " last column at " + rowrec.getLastCol());
			break;
		case NumberRecord.sid:
			NumberRecord numrec = (NumberRecord) record;
            System.out.println("Cell found with value " + numrec.getValue()  + " at row " + numrec.getRow() + " and column " + numrec.getColumn());
            break;
		case SSTRecord.sid:
			sstrec = (SSTRecord) record;
			for (int k = 0; k < sstrec.getNumUniqueStrings(); k++) {
				System.out.println("String table value " + k + " = " + sstrec.getString(k));
			}
            break;
		case LabelSSTRecord.sid:
			LabelSSTRecord lrec = (LabelSSTRecord) record;
            System.out.println("String cell found with value " + sstrec.getString(lrec.getSSTIndex()));
            break;
		}
	}

}
