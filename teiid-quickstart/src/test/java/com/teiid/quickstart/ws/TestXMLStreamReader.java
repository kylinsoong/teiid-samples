package com.teiid.quickstart.ws;

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;


public class TestXMLStreamReader {

	public static void main(String[] args) throws Exception {

		String xmlRequest = "<tns:CapitalCity xmlns:tns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode>CNA</sCountryISOCode></tns:CapitalCity>";
		XMLInputFactory factory = XMLInputFactory.newInstance();
		StAXSource source = new StAXSource(factory.createXMLStreamReader(new ByteArrayInputStream(xmlRequest.getBytes())));
		XMLStreamReader reader = source.getXMLStreamReader();
		
		System.out.println(reader);
		
		while (true) {
			if (reader.getEventType() == XMLStreamConstants.END_DOCUMENT) {
				break;
			}
			if(reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
				String cursor = reader.getLocalName();
				if(cursor.equals("sCountryISOCode")){
					reader.next();
					String value = reader.getText();
					System.out.println(value);
					break;
				}
			}
			
			reader.next();
		}
		
	}

}
