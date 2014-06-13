# What's this?

In JBDS, preview data via the Teiid Server, The server throw the following exception

~~~
16:11:51,815 INFO  [org.teiid.CONNECTOR] (Worker1_QueryProcessorQueue1) PostgreSQLExecutionFactory Commit=true;DatabaseProductName=PostgreSQL;DatabaseProductVersion=8.4.20;DriverMajorVersion=8;DriverMajorVersion=4;DriverName=PostgreSQL Native Driver;DriverVersion=PostgreSQL 8.4 JDBC4 (build 704);IsolationLevel=2
16:11:51,888 WARN  [org.teiid.CONNECTOR] (Worker1_QueryProcessorQueue1) Connector worker process failed for atomic-request=epVUOtAVp/6x.0.0.0: org.teiid.translator.jdbc.JDBCExecutionException: 0 TEIID11008:TEIID11004 Error executing statement(s): [Prepared Values: [] SQL: SELECT g_0."productid", g_0."productname", g_0."producttype", g_0."issuer", g_0."exchange", g_0."djicomponent", g_0."sp500component", g_0."nas100component", g_0."amexintcomponent", g_0."primarybusiness" FROM "public"."productdata" AS g_0]
        at org.teiid.translator.jdbc.JDBCQueryExecution.execute(JDBCQueryExecution.java:88)
        at org.teiid.dqp.internal.datamgr.ConnectorWorkItem.execute(ConnectorWorkItem.java:312) [teiid-engine-8.4.1-redhat-7.jar:8.4.1-redhat-7]
        at org.teiid.dqp.internal.process.DataTierTupleSource.getResults(DataTierTupleSource.java:301) [teiid-engine-8.4.1-redhat-7.jar:8.4.1-redhat-7]
        at org.teiid.dqp.internal.process.DataTierTupleSource$1.call(DataTierTupleSource.java:113) [teiid-engine-8.4.1-redhat-7.jar:8.4.1-redhat-7]
        at org.teiid.dqp.internal.process.DataTierTupleSource$1.call(DataTierTupleSource.java:110) [teiid-engine-8.4.1-redhat-7.jar:8.4.1-redhat-7]
        at java.util.concurrent.FutureTask.run(FutureTask.java:262) [rt.jar:1.7.0_55]
        at org.teiid.dqp.internal.process.FutureWork.run(FutureWork.java:58) [teiid-engine-8.4.1-redhat-7.jar:8.4.1-redhat-7]
        at org.teiid.dqp.internal.process.DQPWorkContext.runInContext(DQPWorkContext.java:269) [teiid-engine-8.4.1-redhat-7.jar:8.4.1-redhat-7]
        at org.teiid.dqp.internal.process.ThreadReuseExecutor$RunnableWrapper.run(ThreadReuseExecutor.java:119) [teiid-engine-8.4.1-redhat-7.jar:8.4.1-redhat-7]
        at org.teiid.dqp.internal.process.ThreadReuseExecutor$3.run(ThreadReuseExecutor.java:214) [teiid-engine-8.4.1-redhat-7.jar:8.4.1-redhat-7]
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145) [rt.jar:1.7.0_55]
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615) [rt.jar:1.7.0_55]
        at java.lang.Thread.run(Thread.java:744) [rt.jar:1.7.0_55]
Caused by: org.postgresql.util.PSQLException: ERROR: permission denied for relation productdata
        at org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse(QueryExecutorImpl.java:2094)
        at org.postgresql.core.v3.QueryExecutorImpl.processResults(QueryExecutorImpl.java:1827)
        at org.postgresql.core.v3.QueryExecutorImpl.execute(QueryExecutorImpl.java:255)
        at org.postgresql.jdbc2.AbstractJdbc2Statement.execute(AbstractJdbc2Statement.java:508)
        at org.postgresql.jdbc2.AbstractJdbc2Statement.executeWithFlags(AbstractJdbc2Statement.java:384)
        at org.postgresql.jdbc2.AbstractJdbc2Statement.executeQuery(AbstractJdbc2Statement.java:269)
        at org.jboss.jca.adapters.jdbc.WrappedPreparedStatement.executeQuery(WrappedPreparedStatement.java:462)
        at org.teiid.translator.jdbc.JDBCQueryExecution.execute(JDBCQueryExecution.java:84)
        ... 12 more

16:11:51,893 WARN  [org.teiid.PROCESSOR] (Worker0_QueryProcessorQueue2) TEIID30020 Processing exception for request epVUOtAVp/6x.0 'TEIID30504 Products: 0 TEIID11008:TEIID11004 Error executing statement(s): [Prepared Values: [] SQL: SELECT g_0."productid", g_0."productname", g_0."producttype", g_0."issuer", g_0."exchange", g_0."djicomponent", g_0."sp500component", g_0."nas100component", g_0."amexintcomponent", g_0."primarybusiness" FROM "public"."productdata" AS g_0]'. Originally TeiidProcessingException 'ERROR: permission denied for relation productdata' QueryExecutorImpl.java:2094. Enable more detailed logging to see the entire stacktrace.
~~~

this project for sovling this issue
