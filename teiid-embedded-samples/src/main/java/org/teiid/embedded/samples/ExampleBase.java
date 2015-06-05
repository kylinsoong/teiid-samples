package org.teiid.embedded.samples;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stax.StAXSource;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

public abstract class ExampleBase {
	
	static {

        System.setProperty("java.util.logging.config.file", "src/test/resources/logging.properties");
	}
	
	protected EmbeddedServer server = null;
	protected Connection conn = null;
	
	protected void init(String name, ExecutionFactory<?, ?> factory) throws TranslatorException {
		server = new EmbeddedServer();
		factory.start();
		factory.setSupportsDirectQueryProcedure(true);
		server.addTranslator(name, factory);
	}
	
	protected void start(boolean isRemote) throws Exception{
		
		if(isRemote) {
			SocketConfiguration s = new SocketConfiguration();
			InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
			s.setBindAddress(addr.getHostName());
			s.setPortNumber(addr.getPort());
			s.setProtocol(WireProtocol.teiid);
			EmbeddedConfiguration config = new EmbeddedConfiguration();
			config.setTransactionManager(EmbeddedHelper.getTransactionManager());
			config.addTransport(s);
			server.start(config);
		} else {
			EmbeddedConfiguration config = new EmbeddedConfiguration();
			config.setTransactionManager(EmbeddedHelper.getTransactionManager());
			server.start(config);
		}
	}
	
	public void tearDown() throws Exception {
		if(null != conn) {
			conn.close();
			conn = null;
		}
		if(null != server) {
			server.stop();
			server = null;
		}
	}
	
	protected StAXSource formStAXSource(String xml) throws XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		StAXSource source = new StAXSource(factory.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes())));
		return source;
	}
	
	public abstract void execute() throws Exception;

}
