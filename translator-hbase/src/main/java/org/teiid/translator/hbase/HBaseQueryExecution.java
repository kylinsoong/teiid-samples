package org.teiid.translator.hbase;

import java.util.List;

import org.teiid.language.QueryExpression;
import org.teiid.language.Select;
import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.resource.adapter.hbase.HBaseConnection;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.TranslatorException;

public class HBaseQueryExecution extends HBaseExecution implements ResultSetExecution {
	
	private Select command;
	private Class<?>[] expectedTypes;

	public HBaseQueryExecution(HBaseExecutionFactory executionFactory
							 , QueryExpression command
							 , ExecutionContext executionContext
							 , RuntimeMetadata metadata
							 , HBaseConnection hbconnection) {
		super(executionFactory, executionContext, metadata, hbconnection);
		this.command = (Select) command;
		this.expectedTypes = command.getColumnTypes();
	}
	
	@Override
	public void execute() throws TranslatorException {

		LogManager.logInfo(LogConstants.CTX_CONNECTOR, this.command);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() throws TranslatorException {
		// TODO Auto-generated method stub

	}

	

	@Override
	public List<?> next() throws TranslatorException, DataNotAvailableException {
		// TODO Auto-generated method stub
		return null;
	}

}
