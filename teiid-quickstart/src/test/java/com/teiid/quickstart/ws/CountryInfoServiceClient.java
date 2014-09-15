package com.teiid.quickstart.ws;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;

import org.w3c.dom.Document;

public class CountryInfoServiceClient {
	
	static final String WSDL = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL" ;
	static final String PORT = "CountryInfoServiceSoap" ;
	static final String NAMESPACEURI = "http://www.oorsprong.org/websamples.countryinfo";
	static final String SERVICE = "CountryInfoService";
	
	public static void main(String[] args) throws Exception {
		
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
