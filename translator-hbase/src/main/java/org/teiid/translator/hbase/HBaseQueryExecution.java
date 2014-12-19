package org.teiid.translator.hbase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.teiid.language.NamedTable;
import org.teiid.language.QueryExpression;
import org.teiid.language.Select;
import org.teiid.language.TableReference;
import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.metadata.Table;
import org.teiid.resource.adapter.hbase.HBaseConnection;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.TranslatorException;

public class HBaseQueryExecution extends HBaseExecution implements ResultSetExecution {
	
	private Class<?>[] columnDataTypes;
	
	protected ResultSet results;
	
	private SQLConversionVisitor vistor;

	public HBaseQueryExecution(HBaseExecutionFactory executionFactory
							 , QueryExpression command
							 , ExecutionContext executionContext
							 , RuntimeMetadata metadata
							 , HBaseConnection hbconnection) throws HBaseExecutionException {
		super(command, executionFactory, executionContext, metadata, hbconnection);
		this.columnDataTypes = command.getColumnTypes();
		
		vistor = this.executionFactory.getSQLConversionVisitor();
		vistor.visitNode(command);
		
		phoenixTableMapping(vistor.getMappingDDLList());
	}

	@Override
	public void execute() throws TranslatorException {

		LogManager.logInfo(LogConstants.CTX_CONNECTOR, this.command);
		
		boolean usingTxn = false;
		boolean success = false;
		try {
			results = getStatement().executeQuery(vistor.getSQL());
			success = true;
		} catch (SQLException e) {
			throw new HBaseExecutionException(HBasePlugin.Event.TEIID27002, e, command);
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
			throw new HBaseExecutionException(HBasePlugin.Event.TEIID27002, e, HBasePlugin.Event.TEIID27011, command);
		}
		
		return null;
	}

	@Override
	public void close() {

		if (results != null) {
            try {
                results.close();
                results = null;
            } catch (SQLException e) {
            	LogManager.logDetail(LogConstants.CTX_CONNECTOR, e, "Exception closing ResultSet"); 
            }
        }
		
		super.close();
	}
	

}
