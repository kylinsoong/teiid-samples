package org.teiid.translator.hbase;

import java.util.List;

import org.teiid.language.Command;
import org.teiid.language.Literal;
import org.teiid.language.Parameter;
import org.teiid.language.visitor.CollectorVisitor;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.TypeFacility;

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
		sqlConversionVisitor.setExecutionContext(context);
		if (executionFactory.usePreparedStatements() || hasBindValue(command)) {
			sqlConversionVisitor.setPrepared(true);
		}
		sqlConversionVisitor.append(command);
		this.sql = sqlConversionVisitor.toString();
		this.preparedValues = sqlConversionVisitor.getPreparedValues();
		this.prepared = sqlConversionVisitor.isPrepared();
	}

	private boolean hasBindValue(Command command) {
		if (!CollectorVisitor.collectObjects(Parameter.class, command).isEmpty()) {
            return true;
        }
        for (Literal l : CollectorVisitor.collectObjects(Literal.class, command)) {
            if (isBindEligible(l)) {
                return true;
            }
        }
        return false;
	}

	private boolean isBindEligible(Literal l) {
		return TypeFacility.RUNTIME_TYPES.XML.equals(l.getType())
				|| TypeFacility.RUNTIME_TYPES.CLOB.equals(l.getType())
				|| TypeFacility.RUNTIME_TYPES.BLOB.equals(l.getType())
				|| TypeFacility.RUNTIME_TYPES.OBJECT.equals(l.getType());
	}

	public String getSql() {
		return sql;
	}

	public boolean isPrepared() {
		return prepared;
	}

	public List getPreparedValues() {
		return preparedValues;
	}

}
