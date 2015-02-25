package org.teiid.test.jira;

import java.sql.Connection;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;

import org.teiid.translator.ExecutionFactory;

public class TEIID_3346 {

	public static void main(String[] args) throws Exception {
		
		EmbeddedServer es = new EmbeddedServer();
		es.start(new EmbeddedConfiguration());
		
		es.addTranslator("tranlator-void", new ExecutionFactory<Void, Void>());
		
		ModelMetaData mmd1 = new ModelMetaData();
		mmd1.setName("TEIID-3346");
		mmd1.setSchemaSourceType("ddl");
		mmd1.setSchemaText("create foreign table test (x.y string)");
		mmd1.addSourceMapping("void", "tranlator-void", "java:/test-ds");
		
		es.deployVDB("vdb", mmd1);
		
		Connection c = es.getDriver().connect("jdbc:teiid:vdb", null);
		System.out.println(c);

	}

}
