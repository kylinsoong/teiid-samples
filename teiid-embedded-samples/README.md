## What's this

Teiid Embedded is a light-weight version of Teiid, it contain an easy-to-use JDBC Driver that can embed the Query Engine in any Java application. The Embedded mode supply almost all Teiid features without JEE Container involved, it supply a convenient way for Users who want integrate Teiid with their Application.

Primary purpose of this project contains:

* Demonstrating how simple Java Application interect multiple, heterogenous data sources/stores(Relational Databases, NO-SQL, In-memory Data Grid/Cache, Flat Files, Web Services, etc) with Teiid Embedded.
* Demonstrating how to develop new Translator/Connector for Teiid new Users or Teiid starter 

### Available samples

The following tables show all available samples:

| **Data Sources** | **VDB file** | **Code View** | **Blog Link** |
|:-----------------|:-------------|:------------------|:--------------|
|![CSV](metadata/img/csv.jpeg) ![XML](metadata/img/xml.jpeg) ![TXT](metadata/img/text_icon.gif) |[files-vdb.xml](vdb/files-vdb.xml) |[TestFileDataSource](src/test/java/com/teiid/embedded/samples/file/TestFileDataSource.java) |[link](http://ksoong.org/teiid-embedded-file) |
|![Excel](metadata/img/excel.jpeg) |[excel-vdb.xml](vdb/excel-vdb.xml) |[TestExcelDataSource](src/test/java/com/teiid/embedded/samples/excel/TestExcelDataSource.java) |[link](http://ksoong.org/teiid-embedded-excel) |
|![Mysql](metadata/img/mysql-icon.png) |[mysql-vdb.xml](vdb/mysql-vdb.xml) |[TestMysqDataSource](src/test/java/com/teiid/embedded/samples/mysql/TestMysqDataSource.java) |[link](http://ksoong.org/teiid-embedded-mysql) |
|![H2](metadata/img/h2-icon.png) |[h2-vdb.xml](vdb/h2-vdb.xml) |[TestH2DataSource](src/test/java/com/teiid/embedded/samples/h2/TestH2DataSource.java) |[link](http://ksoong.org/teiid-embedded-h2) |
|![Infnispan](metadata/img/infinispan.jpeg) |[infinispancache-vdb.xml](vdb/infinispancache-vdb.xml) |[TestInfinispanLocalCache](src/test/java/com/teiid/embedded/samples/infinispan/TestInfinispanLocalCache.java) |[link](http://ksoong.org/teiid-embedded-cache) | 
|![Infnispan](metadata/img/infinispan.jpeg) |[remotecache-vdb.xml](vdb/remotecache-vdb.xml) |[TestInfinispanRemoteCache](src/test/java/com/teiid/embedded/samples/infinispan/TestInfinispanRemoteCache.java) |[link](http://ksoong.org/teiid-embedded-grid) |
|![Cassandra](metadata/img/cassandra.gif) |[cassandra-vdb.xml](vdb/cassandra-vdb.xml) |[TestCassandraDataSource](src/test/java/com/teiid/embedded/samples/cassandra/TestCassandraDataSource.java) |[link](http://ksoong.org/teiid-cassandra) |
|![MongoDB](metadata/img/mongodb.jpeg) |[mongodb-vdb.xml](vdb/mongodb-vdb.xml) |[TestMongoDBDataSource](src/test/java/com/teiid/embedded/samples/mongodb/TestMongoDBDataSource.java) |[link](http://ksoong.org/teiid-mongodb) |
|REST WebService |[restwebservice-vdb.xml](vdb/restwebservice-vdb.xml) |[TestRESTWebServiceDataSource](src/test/java/com/teiid/embedded/samples/ws/TestRESTWebServiceDataSource.java) |[link](http://ksoong.org/teiid-restws) |
|WebService |[webservice-vdb.xml](vdb/webservice-vdb.xml) |[TestWebServiceDataSource](src/test/java/com/teiid/embedded/samples/ws/TestWebServiceDataSource.java) |[link](http://ksoong.org/teiid-ws) |
|![OpenLDAP](metadata/img/logo-openldap.png) |[ldap-vdb.xml](vdb/ldap-vdb.xml) |[TestLDAPDataSource](src/test/java/com/teiid/embedded/samples/ldap/TestLDAPDataSource.java) |[link](http://ksoong.org/teiid-ldap) |


## How to integrate Teiid with Java Application.

### Step I: Add Dependencies

The `teiid-runtime` is necessary, it contain Teiid Embedded libraries, you can either download Teiid Embedded from [http://teiid.jboss.org/downloads/](http://teiid.jboss.org/downloads/), or add maven dependency:

~~~
		<dependency>
			<groupId>org.jboss.teiid</groupId>
			<artifactId>teiid-runtime</artifactId>
			<version>8.9.0.Alpha2-SNAPSHOT</version>
		</dependency>
~~~

The other dependencies should be connecor/translator which depend on the the data sources your are using.

### Step II: Start Teiid Embedded Server with connecor/translator configured

The following code snippets show a rough process:

~~~
EmbeddedServer server = new EmbeddedServer();
server.addTranslator("my-translator", myExecutionFactory);
server.addConnectionFactory("my-connector", myConnectionFactory)
server.start(config);
~~~

### Step III: Deploy VDB

Use Embedded Server API to deploy VDB:

~~~
server.deployVDB(new FileInputStream(new File("my-vdb.xml")));
~~~

### Step IV: Consume the data

Teiid Embedded have an easy-to-use JDBC Driver, we consume the data via it:

~~~
Connection conn = server.getDriver().connect("jdbc:teiid:MyVDB", null);
~~~


