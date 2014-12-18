package org.teiid.translator.hbase;

import org.teiid.language.Command;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.resource.adapter.hbase.HBaseConnection;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.UpdateExecution;

public class HBaseUpdateExecution extends HBaseExecution implements UpdateExecution {
	
	private Command command;
	private int[] result;
	private int maxPreparedInsertBatchSize;
	
	private SQLConversionVisitor vistor;
	
	public HBaseUpdateExecution(HBaseExecutionFactory executionFactory
							  , Command command
							  , ExecutionContext executionContext
							  , RuntimeMetadata metadata
							  , HBaseConnection connection) {
		super(executionFactory, executionContext, metadata, connection);
		this.command = command;
		this.maxPreparedInsertBatchSize = this.executionFactory.getMaxPreparedInsertBatchSize();
		
		vistor = this.executionFactory.getSQLConversionVisitor();
		vistor.visitNode(command);
	}

	@Override
	public void execute() throws TranslatorException {
		
	}

	@Override
	public int[] getUpdateCounts() throws DataNotAvailableException,
			TranslatorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() throws TranslatorException {
		// TODO Auto-generated method stub

	}

	

}
