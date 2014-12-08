package org.teiid.translator.hbase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	private Class<?>[] columnDataTypes;
	
	protected ResultSet results;

	public HBaseQueryExecution(HBaseExecutionFactory executionFactory
							 , QueryExpression command
							 , ExecutionContext executionContext
							 , RuntimeMetadata metadata
							 , HBaseConnection hbconnection) {
		super(executionFactory, executionContext, metadata, hbconnection);
		this.command = (Select) command;
		this.columnDataTypes = command.getColumnTypes();
	}
	
	@Override
	public void execute() throws TranslatorException {

		LogManager.logInfo(LogConstants.CTX_CONNECTOR, this.command);
		
		boolean usingTxn = false;
		boolean success = false;
		try {
			TranslatedCommand translatedComm = null;
			
			translatedComm = translateCommand(command);
			String sql = translatedComm.getSql();
			
			if (!translatedComm.isPrepared()) {
			    results = getStatement().executeQuery(sql);
			} else {
				PreparedStatement pstatement = getPreparedStatement(sql);
				bind(pstatement, translatedComm.getPreparedValues(), null);
				results = pstatement.executeQuery();
				success = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (usingTxn) {
	        	try {
		        	try {
			        	if (success) {
			        		connection.commit();
			        	} else {
			        		connection.rollback();
			        	}
		        	} finally {
			    		connection.setAutoCommit(true);
		        	}
	        	} catch (SQLException e) {
	        	}
        	}
		}
	}
	
	@Override
	public List<?> next() throws TranslatorException, DataNotAvailableException {
		
		try {
			if (results.next()) {
				List<Object> vals = new ArrayList<Object>(columnDataTypes.length);
				
				for (int i = 0; i < columnDataTypes.length; i++) {
                    // Convert from 0-based to 1-based
                    Object value = this.executionFactory.retrieveValue(results, i+1, columnDataTypes[i]);
                    vals.add(value); 
                }

                return vals;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
