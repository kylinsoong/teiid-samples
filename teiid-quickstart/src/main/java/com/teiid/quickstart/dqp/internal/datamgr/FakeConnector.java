package com.teiid.quickstart.dqp.internal.datamgr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.teiid.language.Command;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.Execution;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.UpdateExecution;

public class FakeConnector extends ExecutionFactory<Object, Object> {

	private int connectionCount;
    private int executionCount;
    private int closeCount;
    
    public int getConnectionCount() {
		return connectionCount;
	}
    
    public int getExecutionCount() {
		return executionCount;
	}
    
    public int getCloseCount() {
		return closeCount;
	}

	@Override
	public Object getConnection(Object factory,ExecutionContext executionContext) throws TranslatorException {
		connectionCount++;
    	return factory;
	}

	@Override
	public void closeConnection(Object connection, Object factory) {
		closeCount ++ ;
	}

	@Override
	public Execution createExecution(Command command, ExecutionContext executionContext, RuntimeMetadata metadata, Object connection) throws TranslatorException {
		executionCount ++ ;
        return new FakeExecution(executionContext);
	}
	
	public final class FakeExecution implements ResultSetExecution, UpdateExecution {
		
		private int rowCount;
        ExecutionContext ec;
        
        public FakeExecution(ExecutionContext ec) {
        	this.ec = ec ;
        }

		@Override
		public void close() {			
		}

		@Override
		public void cancel() throws TranslatorException {			
		}

		@Override
		public void execute() throws TranslatorException {
			ec.addWarning(new Exception("Some warning")); 
		}

		@Override
		public int[] getUpdateCounts() throws DataNotAvailableException, TranslatorException {
			return new int[] {1};
		}

		@Override
		public List<?> next() throws TranslatorException, DataNotAvailableException {
			if (this.rowCount == 1) {
            	return null;
            }
            this.rowCount++;
            return new ArrayList<Object>(Arrays.asList(this.rowCount - 1));
		}
		
	}
}
