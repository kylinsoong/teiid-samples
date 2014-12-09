package org.teiid.translator.hbase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.resource.cci.ConnectionFactory;
import javax.sql.rowset.serial.SerialStruct;

import org.teiid.core.types.ArrayImpl;
import org.teiid.language.Command;
import org.teiid.language.LanguageObject;
import org.teiid.language.QueryExpression;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.resource.adapter.hbase.HBaseConnection;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.Translator;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.TypeFacility;

@Translator(name="hbase", description="HBase Translator, reads and writes the data to HBase")
public class HBaseExecutionFactory extends ExecutionFactory<ConnectionFactory, HBaseConnection> {
	
	private static final Map<Class<?>, Integer> TYPE_CODE_MAP = new HashMap<Class<?>, Integer>();
    
	private StructRetrieval structRetrieval = StructRetrieval.OBJECT;
	
    private static final int INTEGER_CODE = 0;
    private static final int LONG_CODE = 1;
    private static final int DOUBLE_CODE = 2;
    private static final int BIGDECIMAL_CODE = 3;
    private static final int SHORT_CODE = 4;
    private static final int FLOAT_CODE = 5;
    private static final int TIME_CODE = 6;
    private static final int DATE_CODE = 7;
    private static final int TIMESTAMP_CODE = 8;
    private static final int BLOB_CODE = 9;
    private static final int CLOB_CODE = 10;
    private static final int BOOLEAN_CODE = 11;

	public HBaseExecutionFactory() {
		
	}
	
	public enum StructRetrieval {
    	OBJECT,
    	COPY,
    	ARRAY
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

	public List<?> translate(LanguageObject obj, ExecutionContext context) {
		List<?> parts = null;
		if(obj instanceof Command) {
			parts = translateCommand((Command)obj, context);
		}
		
		return parts;
	}
	
	public List<?> translateCommand(Command command, ExecutionContext context) {
    	return null;
    }

	public void bindValue(PreparedStatement stmt, Object param, Class<?> paramType, int i) throws SQLException {

		int type = TypeFacility.getSQLTypeFromRuntimeType(paramType);
		if (param == null) {
            stmt.setNull(i, type);
            return;
        } 
	}

	public Object retrieveValue(ResultSet results, int columnIndex, Class<?> expectedType) throws SQLException {
		Integer code = TYPE_CODE_MAP.get(expectedType);
        if(code != null) {
            switch(code.intValue()) {
                case INTEGER_CODE:  {
                    int value = results.getInt(columnIndex);                    
                    if(results.wasNull()) {
                        return null;
                    }
                    return Integer.valueOf(value);
                }
                case LONG_CODE:  {
                    long value = results.getLong(columnIndex);                    
                    if(results.wasNull()) {
                        return null;
                    } 
                    return Long.valueOf(value);
                }                
                case DOUBLE_CODE:  {
                    double value = results.getDouble(columnIndex);                    
                    if(results.wasNull()) {
                        return null;
                    } 
                    return Double.valueOf(value);
                }                
                case BIGDECIMAL_CODE:  {
                    return results.getBigDecimal(columnIndex); 
                }
                case SHORT_CODE:  {
                    short value = results.getShort(columnIndex);                    
                    if(results.wasNull()) {
                        return null;
                    }                    
                    return Short.valueOf(value);
                }
                case FLOAT_CODE:  {
                    float value = results.getFloat(columnIndex);                    
                    if(results.wasNull()) {
                        return null;
                    } 
                    return Float.valueOf(value);
                }
                case TIME_CODE: {
            		return results.getTime(columnIndex);
                }
                case DATE_CODE: {
            		return results.getDate(columnIndex);
                }
                case TIMESTAMP_CODE: {
            		return results.getTimestamp(columnIndex);
                }
    			case BLOB_CODE: {
    				try {
    					return results.getBlob(columnIndex);
    				} catch (SQLException e) {
    					// ignore
    				}
    				try {
    					return results.getBytes(columnIndex);
    				} catch (SQLException e) {
    					// ignore
    				}
    				break;
    			}
    			case CLOB_CODE: {
    				try {
    					return results.getClob(columnIndex);
    				} catch (SQLException e) {
    					// ignore
    				}
    				break;
    			}  
    			case BOOLEAN_CODE: {
    				return results.getBoolean(columnIndex);
    			}
            }
        }

        Object result = results.getObject(columnIndex);
        if (expectedType == TypeFacility.RUNTIME_TYPES.OBJECT) {
        	return convertObject(result);
        }
		return result;
	}
	
	protected Object convertObject(Object object) throws SQLException {
    	if (object instanceof Struct) {
    		switch (structRetrieval) {
    		case OBJECT:
    			return object;
    		case ARRAY:
    			return new ArrayImpl(((Struct)object).getAttributes());
    		case COPY:
    			return new SerialStruct((Struct)object, Collections.<String, Class<?>> emptyMap());
    		}
    	}
    	return object;
	}
}
