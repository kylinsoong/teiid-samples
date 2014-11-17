package com.teiid.quickstart.excel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class FromHowTo {

	public static void main(String[] args) throws Exception {
		FromHowTo howto = new FromHowTo();
		howto.processOneSheet("src/file/otherholdings.xls");
//		howto.processAllSheets(args[0]);
	}

	private void processOneSheet(String filename) throws IOException, OpenXML4JException, SAXException {

		OPCPackage pkg = OPCPackage.open(filename, PackageAccess.READ);
//		XSSFReader r = new XSSFReader( pkg );
//		SharedStringsTable sst = r.getSharedStringsTable();
//		XMLReader parser = fetchSheetParser(sst);
//		
//		InputStream sheet = r.getSheet("Sheet1");
		
		System.out.println();
	}
	
	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser =
			XMLReaderFactory.createXMLReader(
					"org.apache.xerces.parsers.SAXParser"
			);
		ContentHandler handler = new SheetHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}
	
	private static class SheetHandler extends DefaultHandler {
		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;

		private SheetHandler(SharedStringsTable sst) {
			this.sst = sst;
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			lastContents += new String(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
			
			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if (nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}
			
			// v => contents of a cell
			// Output after we've seen the string contents
			if (name.equals("v")) {
				System.out.println(lastContents);
			}
		}

		@Override
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			
			// c => cell
			if (name.equals("c")) {
				// Print the cell reference
				System.out.print(attributes.getValue("r") + " - ");
				// Figure out if the value is an index in the SST
				String cellType = attributes.getValue("t");
				if (cellType != null && cellType.equals("s")) {
					nextIsString = true;
				} else {
					nextIsString = false;
				}
			}
			// Clear contents cache
			lastContents = "";
		}
	}
}
