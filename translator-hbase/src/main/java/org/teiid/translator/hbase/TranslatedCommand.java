package org.teiid.translator.hbase;

import java.util.List;

import org.teiid.language.Command;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.TranslatorException;

public class TranslatedCommand {

	private String sql;
	private boolean prepared;
	private List preparedValues;
	
	private HBaseExecutionFactory executionFactory;
	private ExecutionContext context;
	
	public TranslatedCommand(ExecutionContext context, HBaseExecutionFactory executionFactory){
		this.executionFactory = executionFactory;
		this.context = context;
	}
	
	public void translateCommand(Command command) throws TranslatorException {
		SQLConversionVisitor sqlConversionVisitor = executionFactory.getSQLConversionVisitor();
//		if (executionFactory.usePreparedStatements() || hasBindValue(command)) {
//			sqlConversionVisitor.setPrepared(true);
//		}
//		sqlConversionVisitor.append(command);
//		this.sql = sqlConversionVisitor.toString();
//		this.preparedValues = sqlConversionVisitor.getPreparedValues();
//		this.prepared = sqlConversionVisitor.isPrepared();
	}

}
