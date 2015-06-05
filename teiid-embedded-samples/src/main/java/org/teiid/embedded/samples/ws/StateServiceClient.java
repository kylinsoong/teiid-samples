package org.teiid.embedded.samples.ws;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.teiid.embedded.samples.util.Util;

/**
 * A CXF Dispatch StAXSource Client
 * 
 * 'StateServiceCXFDispatchStAXSourceClient' depend on StateService, more details refer to
 * 		1. https://github.com/kylinsoong/jaxws/tree/master/stateServic
 * 		2. http://ksoong.org/jaxws-stateservice/
 * 
 * @author kylin
 *
 */
public class StateServiceClient {

	public static void main(String[] args) throws XMLStreamException {

		final QName serviceName = new QName("http://www.teiid.org/stateService/", "stateService");
		final QName portName = new QName("http://www.teiid.org/stateService/", "StateServiceImplPort");
		
		Bus bus = BusFactory.getThreadDefaultBus();
		BusFactory.setThreadDefaultBus(null);
		Service service;
		try {
			service = Service.create(serviceName);
		} finally {
			BusFactory.setThreadDefaultBus(bus);
		}
		
		String bindingId = "http://schemas.xmlsoap.org/wsdl/soap/http";
		String endpointAddress = "http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL";
		service.addPort(portName, bindingId, endpointAddress);
		Dispatch<StAXSource> dispatch = service.createDispatch(portName, StAXSource.class, Mode.PAYLOAD);
				
		StAXSource returnValue = dispatch.invoke(Util.formStAXSource(StateServiceExample.GET_ALL));
		System.out.println(parseResult(returnValue.getXMLStreamReader()));
		
		returnValue = dispatch.invoke(Util.formStAXSource(StateServiceExample.GET_ONE));
		System.out.println(parseResult(returnValue.getXMLStreamReader()));
	}
	
	static List<String> parseResult(XMLStreamReader reader) throws XMLStreamException {
		List<String> stateNames = new ArrayList<String>();
		
		while (true) {
			if (reader.getEventType() == END_DOCUMENT) {
				break;
			}		
			if (reader.getEventType() == START_ELEMENT) {
				String cursor = reader.getLocalName();
				if (cursor.equals("Name")) {
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
