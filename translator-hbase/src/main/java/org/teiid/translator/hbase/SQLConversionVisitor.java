package org.teiid.translator.hbase;

import org.teiid.query.sql.visitor.SQLStringVisitor;
import org.teiid.translator.ExecutionContext;

public class SQLConversionVisitor extends SQLStringVisitor {
	
	private HBaseExecutionFactory executionFactory ;
	private ExecutionContext context ;
	
	private boolean prepared;
	
	public SQLConversionVisitor(HBaseExecutionFactory ef, ExecutionContext context) {
		this.executionFactory = ef;
		this.context = context;
		this.prepared = executionFactory.usePreparedStatements();
	}

}
