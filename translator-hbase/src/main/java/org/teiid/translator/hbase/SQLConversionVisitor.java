package org.teiid.translator.hbase;

import static org.teiid.language.SQLConstants.Reserved.DISTINCT;
import static org.teiid.language.SQLConstants.Reserved.FROM;
import static org.teiid.language.SQLConstants.Reserved.HAVING;
import static org.teiid.language.SQLConstants.Reserved.SELECT;
import static org.teiid.language.SQLConstants.Reserved.WHERE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.phoenix.schema.PColumn;
import org.apache.phoenix.schema.PDataType;
import org.apache.phoenix.schema.PTableImpl;
import org.teiid.language.Argument;
import org.teiid.language.ColumnReference;
import org.teiid.language.DerivedColumn;
import org.teiid.language.LanguageObject;
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

public class SQLConversionVisitor extends SQLStringVisitor implements SQLStringVisitor.Substitutor {
	
	private HBaseExecutionFactory executionFactory ;
	private ExecutionContext context ;
	
	// used to map hbase table to phoenix
	private PTableImpl ptable;
	
	private boolean prepared;
	
	private List preparedValues = new ArrayList();
	
	private Set<LanguageObject> recursionObjects = Collections.newSetFromMap(new IdentityHashMap<LanguageObject, Boolean>());
	private Map<LanguageObject, Object> translations = new IdentityHashMap<LanguageObject, Object>(); 
	
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
	
	public PTableImpl getMappingDDL() {
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
            append(obj.getFrom());
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
		
		Map<String, List<String>> quaMap = new HashMap<String, List<String>> ();
		String tname = table.getProperty(HBaseMetadataProcessor.TABLE, false);
		
		List<PColumn> columns = new ArrayList<PColumn>();
		for(Column column : table.getColumns()) {
			PColumn pcolumn;
			String cell = column.getProperty(HBaseMetadataProcessor.CELL, false);
			String[] qua =  cell.split(":");
			if(qua.length != 2) {
				String rname = cell;
				pcolumn = new PColumnTeiidImpl(PNameTeiidImpl.makePName(cell), null, convertType(column));
			} else {
				
				if(quaMap.get(qua[0]) == null) {
					List<String> qualist = new ArrayList<String>();
					quaMap.put(qua[0], qualist);
				}
				quaMap.get(qua[0]).add(qua[1]);
				pcolumn = new PColumnTeiidImpl(PNameTeiidImpl.makePName(qua[1]), PNameTeiidImpl.makePName(qua[0]), convertType(column));
			}	
			columns.add(pcolumn);
		}
		
		String rname = null ;
		
					
			
			
			
		
		
//		this.mappingDDL = PhoenixUtils.hbaseTableMappingDDL(tname, rname, quaMap);
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
	public void visit(ColumnReference obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
	}

	@Override
	public void visit(DerivedColumn obj) {
		// TODO Auto-generated method stub
		super.visit(obj);
	}

	@Override
	public void append(LanguageObject obj) {
		
		List<?> parts = null;
		if (!recursionObjects.contains(obj)) {
			Object trans = this.translations.get(obj);
    		if (trans instanceof List<?>) {
    			parts = (List<?>)trans;
    		} else if (trans instanceof LanguageObject) {
    			obj = (LanguageObject)trans;
    		} else {
    			parts = executionFactory.translate(obj, context);
    		}
		}
		
		if(parts != null) {
			recursionObjects.add(obj);
			for (Object part : parts) {
				if(part instanceof LanguageObject) {
			        append((LanguageObject)part);
			    } else {
			        buffer.append(part);
			    }
			}
			recursionObjects.remove(obj);
		} else {
			super.append(obj);
		}
	}

	@Override
	public void substitute(Argument arg, StringBuilder builder, int index) {
		// TODO Auto-generated method stub
		
	}

}
