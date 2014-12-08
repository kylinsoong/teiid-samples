package org.teiid.translator.hbase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.teiid.language.Argument;
import org.teiid.language.LanguageObject;
import org.teiid.language.visitor.SQLStringVisitor;
import org.teiid.translator.ExecutionContext;

public class SQLConversionVisitor extends SQLStringVisitor implements SQLStringVisitor.Substitutor {
	
	private HBaseExecutionFactory executionFactory ;
	private ExecutionContext context ;
	
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
