package org.teiid.resource.adapter.hbase;

import java.sql.Connection;

public interface HBaseConnection {

	Connection getConnection();
}
