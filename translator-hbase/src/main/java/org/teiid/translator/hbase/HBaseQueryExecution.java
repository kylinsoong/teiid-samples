package org.teiid.translator.hbase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.teiid.language.DerivedTable;
import org.teiid.language.Join;
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
		
		SQLConversionVisitor vistor = new SQLConversionVisitor(executionFactory);
		vistor.visitNode(command);
//		phoenixTableCreation();
	}
	
	@Override
	protected List<Table> getlMetaDataTable() {
		List<TableReference> list = command.getFrom();
		List<Table> namelist = new ArrayList<Table>();
		for(TableReference reference : list) {
			if(reference instanceof NamedTable) {
				NamedTable namedtable = (NamedTable) reference;
				Table table = namedtable.getMetadataObject();
				namelist.add(table);
			} 
		}
		return namelist;
	}
	
	@Override
	public void execute() throws TranslatorException {
		
		TranslatedCommand translatedComm = null;
		
		translatedComm = translateCommand(command);
		String sql = translatedComm.getSql();
		
		System.out.println(sql);

//		LogManager.logInfo(LogConstants.CTX_CONNECTOR, this.command);
//		
//		boolean usingTxn = false;
//		boolean success = false;
//		try {
//			TranslatedCommand translatedComm = null;
//			
//			translatedComm = translateCommand(command);
//			String sql = translatedComm.getSql();
//			
//			if (!translatedComm.isPrepared()) {
//			    results = getStatement().executeQuery(sql);
//			} else {
//				PreparedStatement pstatement = getPreparedStatement(sql);
//				bind(pstatement, translatedComm.getPreparedValues(), null);
//				results = pstatement.executeQuery();
//				success = true;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			if (usingTxn) {
//	        	try {
//		        	try {
//			        	if (success) {
//			        		connection.commit();
//			        	} else {
//			        		connection.rollback();
//			        	}
//		        	} finally {
//			    		connection.setAutoCommit(true);
//		        	}
//	        	} catch (SQLException e) {
//	        	}
//        	}
//		}
	}
	
	@Override
	public List<?> next() throws TranslatorException, DataNotAvailableException {
		
//		try {
//			if (results.next()) {
//				List<Object> vals = new ArrayList<Object>(columnDataTypes.length);
//				
//				for (int i = 0; i < columnDataTypes.length; i++) {
//                    // Convert from 0-based to 1-based
//                    Object value = this.executionFactory.retrieveValue(results, i+1, columnDataTypes[i]);
//                    vals.add(value); 
//                }
//
//                return vals;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
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
