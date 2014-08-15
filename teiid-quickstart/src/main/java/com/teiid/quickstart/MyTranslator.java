package com.teiid.quickstart;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.teiid.language.Command;
import org.teiid.language.QueryExpression;
import org.teiid.metadata.Column;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.metadata.Table;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.TypeFacility;
import org.teiid.translator.UpdateExecution;

public class MyTranslator extends ExecutionFactory<AtomicInteger, Object> {
	
	public Object getConnection(AtomicInteger factory, ExecutionContext executionContext) throws TranslatorException {
		return factory.incrementAndGet();
	}

	public void closeConnection(Object connection, AtomicInteger factory) {
	
	}

	public void getMetadata(MetadataFactory metadataFactory, Object conn) throws TranslatorException {
		Table t = metadataFactory.addTable("my-table");
		t.setSupportsUpdate(true);
		Column c = metadataFactory.addColumn("my-column", TypeFacility.RUNTIME_NAMES.STRING, t);
		c.setUpdatable(true);
	}

	public ResultSetExecution createResultSetExecution(
			QueryExpression command, ExecutionContext executionContext,
			RuntimeMetadata metadata, Object connection)
			throws TranslatorException {
		
				ResultSetExecution rse = new ResultSetExecution() {
			
					@Override
					public void execute() throws TranslatorException {

					}

					@Override
					public void close() {

					}

					@Override
					public void cancel() throws TranslatorException {

					}

					@Override
					public List<?> next() throws TranslatorException,
							DataNotAvailableException {
						return null;
					}
				};
				return rse;
		}

	public UpdateExecution createUpdateExecution(Command command,
			ExecutionContext executionContext,
			RuntimeMetadata metadata, Object connection)
			throws TranslatorException {
		
			UpdateExecution ue = new UpdateExecution() {
			
			@Override
			public void execute() throws TranslatorException {
				
			}
			
			@Override
			public void close() {
				
			}
			
			@Override
			public void cancel() throws TranslatorException {
				
			}
			
			@Override
			public int[] getUpdateCounts() throws DataNotAvailableException, TranslatorException {
				return new int[] {2};
			}
		};
		return ue;
	}

}
