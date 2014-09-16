package com.teiid.quickstart.ws;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.w3c.dom.Document;

public class CountryInfoServiceClient {
	
	static final String WSDL = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL" ;
	static final String PORT = "CountryInfoServiceSoap" ;
	static final String NAMESPACEURI = "http://www.oorsprong.org/websamples.countryinfo";
	static final String SERVICE = "CountryInfoService";
	
	public static void main(String[] args) throws Exception {
		
		clientWithSoapRequest();
		
		clientWithStaxSource();
		
	}
	
	protected static void clientWithStaxSource() throws Exception{
		
		QName serviceName = new QName(NAMESPACEURI, SERVICE);
		QName portName = new QName(NAMESPACEURI, PORT);

		Bus bus = BusFactory.getThreadDefaultBus();
		BusFactory.setThreadDefaultBus(null);
		Service svc;
		try {
			svc = Service.create(serviceName);
		} finally {
			BusFactory.setThreadDefaultBus(bus);
		}
		
		String bindingId = "http://schemas.xmlsoap.org/wsdl/soap/http";
		String endpointAddress = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL";
		svc.addPort(portName, bindingId, endpointAddress);
		Dispatch<StAXSource> dispatch  = svc.createDispatch(portName, StAXSource.class, Mode.PAYLOAD);
		
		String xmlRequest = "<tns:CapitalCity xmlns:tns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode>CNA</sCountryISOCode></tns:CapitalCity>";
		XMLInputFactory factory = XMLInputFactory.newInstance();
		StAXSource source = new StAXSource(factory.createXMLStreamReader(new ByteArrayInputStream(xmlRequest.getBytes())));
		StAXSource returnValue = dispatch.invoke(source);
		
		PrintResult(returnValue.getXMLStreamReader());
		
	}

	private static void PrintResult(XMLStreamReader reader) throws XMLStreamException {
		while (true) {
			if (reader.getEventType() == XMLStreamConstants.END_DOCUMENT) {
				break;
			}
			if(reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
				String cursor = reader.getLocalName();
				if(cursor.equals("CapitalCityResult")){
					reader.next();
					String value = reader.getText();
					System.out.println(value);
					break;
				}
			}
			
			reader.next();
		}
	}

	protected static void clientWithSoapRequest() throws Exception {
		URL wsdlURL = new URL(WSDL);
		QName serviceName = new QName(NAMESPACEURI, SERVICE);
		QName portName = new QName(NAMESPACEURI, PORT);
		
		final Service service = Service.create(wsdlURL, serviceName);
		Dispatch<SOAPMessage> dispatch = service.createDispatch(portName, SOAPMessage.class, Mode.MESSAGE);
		SOAPMessage response = dispatch.invoke(getRequest());
		Document doc = response.getSOAPBody().extractContentAsDocument();
		printxmldoc(doc);
	}

	private static SOAPMessage getRequest() throws SOAPException, IOException {
		String xml = readFile("src/test/resources/soapRequest.xml");
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
		return message;
	}
	
	private static String readFile( String file ) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	    }
	    
	    reader.close();

	    return stringBuilder.toString();
	}
	
	private static void printxmldoc(Document doc) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		System.out.println(writer.toString());
	}

}
