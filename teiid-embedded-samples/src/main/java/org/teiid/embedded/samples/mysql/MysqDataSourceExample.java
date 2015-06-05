package org.teiid.embedded.samples.mysql;

import java.io.File;
import java.io.FileInputStream;


import javax.sql.DataSource;

import org.teiid.embedded.samples.EmbeddedHelper;
import org.teiid.embedded.samples.ExampleBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

public class MysqDataSourceExample extends ExampleBase{

	@Override
	public void execute() throws Exception {
		
		DataSource ds = EmbeddedHelper.newDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "jdv_user", "jdv_pass");
		
		init("translator-mysql", new MySQL5ExecutionFactory());
		
		server.addConnectionFactory("java:/accounts-ds", ds);
		
		start(false);
		
		server.deployVDB(new FileInputStream(new File("vdb/mysql-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:MysqlVDB", null);	
		
		JDBCUtil.executeQuery(conn, "SELECT AVG(TIMESTAMPDIFF(SQL_TSI_SECOND, tf.start_time, tf.end_time)) FROM time_function_test tf GROUP BY id");
		
		System.out.println(conn);
		
	}
	
	public static void main(String[] args) throws Exception {
		new MysqDataSourceExample().execute();
	}


}
