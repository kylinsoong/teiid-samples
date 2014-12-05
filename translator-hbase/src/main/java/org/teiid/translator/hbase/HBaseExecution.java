package org.teiid.translator.hbase;

import java.sql.Connection;

import org.teiid.language.Command;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.resource.adapter.hbase.HBaseConnection;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.TranslatorException;

public class HBaseExecution {
	
	protected HBaseExecutionFactory executionFactory;
	protected ExecutionContext executionContext;
	protected RuntimeMetadata metadata;
	protected HBaseConnection hbconnection;
	protected Connection connection;
	
	public HBaseExecution(HBaseExecutionFactory executionFactory, ExecutionContext executionContext, RuntimeMetadata metadata, HBaseConnection hbconnection) {
		this.executionFactory = executionFactory;
		this.executionContext = executionContext;
		this.metadata = metadata;
		this.hbconnection = hbconnection;
		this.connection = hbconnection.getConnection();
	}
	
//	protected TranslatedCommand translateCommand(Command command) throws TranslatorException {
//		TranslatedCommand translatedCommand = new TranslatedCommand(executionContext, this.executionFactory);
//		translatedCommand.translateCommand(command);
//		if (translatedCommand.getSql() != null && LogManager.isMessageToBeRecorded(LogConstants.CTX_CONNECTOR, MessageLevel.DETAIL)) {
//		LogManager.logDetail(LogConstants.CTX_CONNECTOR, "Source-specific command: " + translatedCommand.getSql()); //$NON-NLS-1$
//		}
//		return translatedCommand;
//		}

}
