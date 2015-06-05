package org.teiid.embedded.samples.util;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;

public class Util {
	
	public static StAXSource formStAXSource(String xml) throws XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		StAXSource source = new StAXSource(factory.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes())));
		return source;
	}
	
	public static List<String> parse(XMLStreamReader reader, String... elementNames) throws XMLStreamException {
		
		Set<String> set = new HashSet<>();
		for(String name : elementNames){
			set.add(name);
		}
		
		List<String> stateNames = new ArrayList<String>();
		
		while (true) {
			if (reader.getEventType() == END_DOCUMENT) {
				break;
			}
			
			if (reader.getEventType() == START_ELEMENT) {
				String cursor = reader.getLocalName();
				if (set.contains(cursor)) {
					reader.next();
					String value = reader.getText();
					stateNames.add(value);
				}
			}
			
			reader.next();
		}
		
		return stateNames;
	}

}
