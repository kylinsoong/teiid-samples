package com.teiid.quickstart.dqp.internal.datamgr;

import org.teiid.client.RequestMessage;
import org.teiid.dqp.internal.process.DQPWorkContext;
import org.teiid.dqp.message.AtomicRequestMessage;
import org.teiid.dqp.message.RequestID;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.resolver.QueryResolver;
import org.teiid.query.sql.lang.Command;
import org.teiid.query.util.CommandContext;

public class TestConnectorWorkItem {
	
	private static final QueryMetadataInterface EXAMPLE_BQT = RealMetadataFactory.exampleBQTCached();
	
	private static Command helpGetCommand(String sql, QueryMetadataInterface metadata) throws Exception {
		Command command = QueryParser.getQueryParser().parseCommand(sql);
		QueryResolver.resolveCommand(command, metadata);
		return command;
	}
	
	static AtomicRequestMessage createNewAtomicRequestMessage(int requestid, int nodeid) throws Exception {
		RequestMessage rm = new RequestMessage();
		
		DQPWorkContext workContext = RealMetadataFactory.buildWorkContext(EXAMPLE_BQT, RealMetadataFactory.exampleBQTVDB());
		workContext.getSession().setSessionId(String.valueOf(1));
		workContext.getSession().setUserName("foo"); //$NON-NLS-1$
		
		AtomicRequestMessage request = new AtomicRequestMessage(rm, workContext, nodeid);
		request.setCommand(helpGetCommand("SELECT BQT1.SmallA.INTKEY FROM BQT1.SmallA", EXAMPLE_BQT)); //$NON-NLS-1$
		request.setRequestID(new RequestID(requestid));
		request.setConnectorName("testing"); //$NON-NLS-1$
		request.setFetchSize(5);
		request.setCommandContext(new CommandContext());
		return request;
	}

}
