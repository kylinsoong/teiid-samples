# What's this?

Start JDV 6.0.0.GA after installed via

~~~
java -jar jboss-dv-installer-6.0.0.GA-redhat-4.jar 
~~~

JDV throw below exception:

~~~
17:17:35,824 ERROR [org.jboss.msc.service.fail] (MSC service thread 1-8) MSC000001: Failed to start service jboss.teiid.transport.jdbc: org.jboss.msc.service.StartException in service jboss.teiid.transport.jdbc: Failed to start service
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1767) [jboss-msc-1.0.4.GA-redhat-1.jar:1.0.4.GA-redhat-1]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145) [rt.jar:1.7.0_55]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615) [rt.jar:1.7.0_55]
	at java.lang.Thread.run(Thread.java:744) [rt.jar:1.7.0_55]
Caused by: org.teiid.core.TeiidRuntimeException: TEIID40065 Failed to resolve the bind address
	at org.teiid.transport.SocketConfiguration.resolveHostName(SocketConfiguration.java:82)
	at org.teiid.transport.SocketConfiguration.getHostAddress(SocketConfiguration.java:103)
	at org.teiid.transport.SocketListener.<init>(SocketListener.java:55)
	at org.teiid.jboss.TransportService.start(TransportService.java:157)
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.startService(ServiceControllerImpl.java:1811) [jboss-msc-1.0.4.GA-redhat-1.jar:1.0.4.GA-redhat-1]
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1746) [jboss-msc-1.0.4.GA-redhat-1.jar:1.0.4.GA-redhat-1]
	... 3 more

17:17:35,844 ERROR [org.jboss.msc.service.fail] (MSC service thread 1-4) MSC000001: Failed to start service jboss.teiid.transport.odbc: org.jboss.msc.service.StartException in service jboss.teiid.transport.odbc: Failed to start service
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1767) [jboss-msc-1.0.4.GA-redhat-1.jar:1.0.4.GA-redhat-1]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145) [rt.jar:1.7.0_55]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615) [rt.jar:1.7.0_55]
	at java.lang.Thread.run(Thread.java:744) [rt.jar:1.7.0_55]
Caused by: org.teiid.core.TeiidRuntimeException: TEIID40065 Failed to resolve the bind address
	at org.teiid.transport.SocketConfiguration.resolveHostName(SocketConfiguration.java:82)
	at org.teiid.transport.SocketConfiguration.getHostAddress(SocketConfiguration.java:103)
	at org.teiid.transport.SocketListener.<init>(SocketListener.java:55)
	at org.teiid.transport.ODBCSocketListener.<init>(ODBCSocketListener.java:43)
	at org.teiid.jboss.TransportService.start(TransportService.java:181)
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.startService(ServiceControllerImpl.java:1811) [jboss-msc-1.0.4.GA-redhat-1.jar:1.0.4.GA-redhat-1]
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1746) [jboss-msc-1.0.4.GA-redhat-1.jar:1.0.4.GA-redhat-1]
	... 3 more
~~~

This project for solving this issue.

# Resolusion

https://bugzilla.redhat.com/show_bug.cgi?id=1108418

No hostname IP mapping cause InetAddress.getLocalHost() throw java.net.UnknownHostException, this trigger teiid-runtime SocketConfiguration throw UnknownHostException, but the exception be swallowed, instead with the TeiidRuntimeException.

The workaround is: edit /etc/hosts, add hostname IP mapping as below
~~~
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4 kylin.redhat.com
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6 kylin.redhat.com
~~~

