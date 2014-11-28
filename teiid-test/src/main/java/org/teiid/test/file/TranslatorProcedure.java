package org.teiid.test.file;

import java.io.File;
import org.teiid.core.types.ClobImpl;
import org.teiid.core.types.InputStreamFactory.FileInputStreamFactory;

public class TranslatorProcedure {

	public static void main(String[] args) {
		File file = new File("src/file/marketdata.csv");
		FileInputStreamFactory isf = new FileInputStreamFactory(file);
		isf.setLength(file.length());
		ClobImpl clob = new ClobImpl(isf, -1);
	}
}
