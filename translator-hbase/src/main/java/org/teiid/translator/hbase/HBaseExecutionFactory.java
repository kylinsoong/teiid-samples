package org.teiid.translator.hbase;

import javax.resource.cci.ConnectionFactory;

import org.teiid.language.QueryExpression;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.resource.adapter.hbase.HBaseConnection;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.Translator;
import org.teiid.translator.TranslatorException;

@Translator(name="hbase", description="HBase Translator, reads and writes the data to HBase")
public class HBaseExecutionFactory extends ExecutionFactory<ConnectionFactory, HBaseConnection> {

	public HBaseExecutionFactory() {
		
	}

	@Override
	public void start() throws TranslatorException {
		super.start();
	}

	@Override
	public ResultSetExecution createResultSetExecution(QueryExpression command
													 , ExecutionContext executionContext
													 , RuntimeMetadata metadata
													 , HBaseConnection connection) throws TranslatorException {
	
		return new HBaseQueryExecution(this, command, executionContext, metadata, connection);
	}
	
	public SQLConversionVisitor getSQLConversionVisitor() {
		return new SQLConversionVisitor(this);
		}

	public boolean usePreparedStatements() {
		// TODO Auto-generated method stub
		return false;
	}
}
