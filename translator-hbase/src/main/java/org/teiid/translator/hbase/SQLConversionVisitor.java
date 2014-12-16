package org.teiid.translator.hbase;

import static org.teiid.language.SQLConstants.Reserved.AS;
import static org.teiid.language.SQLConstants.Reserved.DISTINCT;
import static org.teiid.language.SQLConstants.Reserved.FROM;
import static org.teiid.language.SQLConstants.Reserved.HAVING;
import static org.teiid.language.SQLConstants.Reserved.SELECT;
import static org.teiid.language.SQLConstants.Reserved.WHERE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.phoenix.schema.PColumn;
import org.apache.phoenix.schema.PDataType;
import org.apache.phoenix.schema.PName;
import org.apache.phoenix.schema.PTable;
import org.teiid.language.Argument;
import org.teiid.language.ColumnReference;
import org.teiid.language.DerivedColumn;
import org.teiid.language.NamedTable;
import org.teiid.language.Select;
import org.teiid.language.TableReference;
import org.teiid.language.SQLConstants.Tokens;
import org.teiid.language.visitor.SQLStringVisitor;
import org.teiid.metadata.Column;
import org.teiid.metadata.Table;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.hbase.phoenix.PColumnTeiidImpl;
import org.teiid.translator.hbase.phoenix.PNameTeiidImpl;
import org.teiid.translator.hbase.phoenix.PTableTeiidImpl;
import org.teiid.translator.hbase.phoenix.PhoenixUtils;

public class SQLConversionVisitor extends SQLStringVisitor implements SQLStringVisitor.Substitutor {
	
	private HBaseExecutionFactory executionFactory ;
	private ExecutionContext context ;
	
	// used to map hbase table to phoenix
	private PTable ptable;
	
	private boolean prepared;
	
	private List preparedValues = new ArrayList();
	
	private Map<Column, PColumn> columnsMap = new HashMap<Column, PColumn> ();
	
	private String mappingDDL; 
	
	public SQLConversionVisitor(HBaseExecutionFactory ef) {
		this.executionFactory = ef;
		this.prepared = executionFactory.usePreparedStatements();
	}

	public void setExecutionContext(ExecutionContext context) {
		this.context = context;
	}
	
	List getPreparedValues() {
        return this.preparedValues;
    }
	
	public boolean isPrepared() {
		return prepared;
	}
    
    public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}
	
	public String getMappingDDL() {
		return mappingDDL;
	}
	
	public PTable getPhoenixTable() {
		return ptable;
	}

	@Override
	public void visit(Select obj) {
		
		if (obj.getFrom() != null && !obj.getFrom().isEmpty()) {
			phoenixTableMapping(obj.getFrom());
		}
		
    	if (obj.getWith() != null) {
    		append(obj.getWith());
    	}
		buffer.append(SELECT).append(Tokens.SPACE);
        if (obj.isDistinct()) {
            buffer.append(DISTINCT).append(Tokens.SPACE);
        }
        if (useSelectLimit() && obj.getLimit() != null) {
            append(obj.getLimit());
            buffer.append(Tokens.SPACE);
        }
        append(obj.getDerivedColumns());
        if (obj.getFrom() != null && !obj.getFrom().isEmpty()) {
        	buffer.append(Tokens.SPACE).append(FROM).append(Tokens.SPACE);    
        	buffer.append(ptable.getTableName().getString()).append(Tokens.SPACE).append(AS).append(Tokens.SPACE).append(ptable.getName().getString());
        }
        if (obj.getWhere() != null) {
            buffer.append(Tokens.SPACE)
                  .append(WHERE)
                  .append(Tokens.SPACE);
            append(obj.getWhere());
        }
        if (obj.getGroupBy() != null) {
            buffer.append(Tokens.SPACE);
            append(obj.getGroupBy());
        }
        if (obj.getHaving() != null) {
            buffer.append(Tokens.SPACE)
                  .append(HAVING)
                  .append(Tokens.SPACE);
            append(obj.getHaving());
        }
        if (obj.getOrderBy() != null) {
            buffer.append(Tokens.SPACE);
            append(obj.getOrderBy());
        }
        if (!useSelectLimit() && obj.getLimit() != null) {
            buffer.append(Tokens.SPACE);
            append(obj.getLimit());
        }
    }
	
	private void phoenixTableMapping(List<TableReference> list) {
		
		Table table = null;
		for(TableReference reference : list) {
			if(reference instanceof NamedTable) {
				NamedTable namedtable = (NamedTable) reference;
				table = namedtable.getMetadataObject();
			} 
		}
		
		if(null == table) {
			return ;
		}
		
		String tname = table.getProperty(HBaseMetadataProcessor.TABLE, false);
		PName tableName = PNameTeiidImpl.makePName(tname);
		
		List<PColumn> columns = new ArrayList<PColumn>();
		for(Column column : table.getColumns()) {
			PColumn pcolumn;
			String cell = column.getProperty(HBaseMetadataProcessor.CELL, false);
			String[] qua =  cell.split(":");
			if(qua.length != 2) {
				pcolumn = new PColumnTeiidImpl(PNameTeiidImpl.makePName(cell), null, convertType(column));
			} else {
				pcolumn = new PColumnTeiidImpl(PNameTeiidImpl.makePName(qua[1]), PNameTeiidImpl.makePName(qua[0]), convertType(column));
			}	
			columns.add(pcolumn);
			columnsMap.put(column, pcolumn);
		}
		
		ptable = PTableTeiidImpl.makeTable(tableName, columns);
			
		this.mappingDDL = PhoenixUtils.hbaseTableMappingDDL(ptable);
	}

	private PDataType convertType(Column column) {
		
		Class<?> clas = column.getJavaType();
		
		if(clas.equals(String.class)){
			return PDataType.VARCHAR;
		} else if (clas.equals(Integer.class)){
			return PDataType.INTEGER;
		}
		
		return null;
	}

	@Override
	public void visit(DerivedColumn obj) {
		
		ColumnReference columnReference = (ColumnReference) obj.getExpression();
		Column column = columnReference.getMetadataObject();
		PColumn pcolumn = columnsMap.get(column);
		buffer.append(ptable.getName().getString() + Tokens.DOT + pcolumn.getName().getString());
	}

	@Override
	public void visit(ColumnReference obj) {
		
		Column column = obj.getMetadataObject();
		PColumn pcolumn = columnsMap.get(column);
		buffer.append(ptable.getName().getString() + Tokens.DOT + pcolumn.getName().getString());
	}

	public String getSQL(){
		return buffer.toString();
	}

	@Override
	public void substitute(Argument arg, StringBuilder builder, int index) {
		// TODO Auto-generated method stub
		
	}

}
