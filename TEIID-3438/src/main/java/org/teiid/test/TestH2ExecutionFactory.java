package org.teiid.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.teiid.core.types.BlobImpl;
import org.teiid.core.types.DataTypeManager;
import org.teiid.core.types.InputStreamFactory;
import org.teiid.translator.Translator;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

@Translator(name="test", description="A translator for open source H2 Database")
public class TestH2ExecutionFactory extends H2ExecutionFactory {

	@Override
	public Object retrieveValue(ResultSet results, int columnIndex, Class<?> expectedType) throws SQLException {
		
		Integer code = DataTypeManager.getTypeCode(expectedType);
		
		if(code.intValue() == DataTypeManager.DefaultTypeCodes.BLOB) {
			return new BlobImpl(new InputStreamFactory () {
				
				@Override
				 public InputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(new byte[0]);
				 }});
		}

		return super.retrieveValue(results, columnIndex, expectedType);
	}
}
