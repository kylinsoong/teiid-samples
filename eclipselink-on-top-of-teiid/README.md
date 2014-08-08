
>  NOTE:  This example relies upon the [dynamicvdb-datafederation](https://github.com/teiid/teiid-quickstarts/tree/master/dynamicvdb-datafederation) example and that it needs to be deployed prior to running this example. Therefore, read the dynamicvdb-datafederation's [README.md](https://github.com/teiid/teiid-quickstarts/tree/master/dynamicvdb-datafederation) and follow its directions before continuing.


## System requirements


If you have not done so, please review the System Requirements in [Teiid Quickstart README](https://github.com/teiid/teiid-quickstarts).

This example produces a WAR that is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.



## Manual setup

* Run the setup in [dynamicvdb-datafederation](https://github.com/teiid/teiid-quickstarts/tree/master/dynamicvdb-datafederation) quick start

* Make sure to start the server, if not already

Open a command line and navigate to the "bin" directory under the root directory of the JBoss server

For Linux:   

~~~
./standalone.sh -c standalone-teiid.xml	
~~~

for Windows: 

~~~
standalone.bat -c standalone-teiid.xml
~~~
	
* VDB Deployment:

Copy the following files to the "<jboss.home>/standalone/deployments" directory

. src/vdb/eclipselink-portfolio-vdb.xml
. src/vdb/eclipselink-portfolio-vdb.xml.dodeploy


* See **Query Demonstrations** below to demonstrate EclipseLink on top of teiid.


## Undeploy artifacts

*  To undeploy the Teiid VDB, delete the 

delete the vdb, hibernate-portfolio-vdb.xml, from the directory "<jboss.home>/standalone/deployments"
	
	
## Query Demonstrations
	
Either run [ProductInfoService](src/main/java/org/teiid/quickstart/eclipselink/service/ProductInfoService.java) as java application will query all productinfo, the output like:

~~~
Query Result:
    Product: (id) 1002 (symbol) BA (companyName) The Boeing Company (price) 42.75
    Product: (id) 1003 (symbol) MON (companyName) Monsanto Company (price) 78.75
    Product: (id) 1004 (symbol) PNRA (companyName) Panera Bread Company (price) 84.97
    Product: (id) 1005 (symbol) SY (companyName) Sybase Incorporated (price) 24.30
    Product: (id) 1006 (symbol) BTU (companyName) Peabody Energy (price) 41.25
    Product: (id) 1007 (symbol) IBM (companyName) International Business Machines Corporation (price) 80.89
    Product: (id) 1008 (symbol) DELL (companyName) Dell Computer Corporation (price) 10.75
    Product: (id) 1010 (symbol) HPQ (companyName) Hewlett-Packard Company (price) 31.52
    Product: (id) 1012 (symbol) GE (companyName) General Electric Company (price) 16.45
    Product: (id) 1013 (symbol) MRK (companyName) Merck and Company Incorporated (price) 27.20
    Product: (id) 1014 (symbol) DIS (companyName) Walt Disney Company (price) 20.53
    Product: (id) 1015 (symbol) MCD (companyName) McDonalds Corporation (price) 54.55
    Product: (id) 1016 (symbol) DOW (companyName) Dow Chemical Company (price) 21.80
    Product: (id) 1018 (symbol) GM (companyName) General Motors Corporation (price) 3.15
    Product: (id) 1024 (symbol) SBGI (companyName) Sinclair Broadcast Group Incorporated (price) 2.19
    Product: (id) 1025 (symbol) COLM (companyName) Columbia Sportsware Company (price) 33.89
    Product: (id) 1026 (symbol) COLB (companyName) Columbia Banking System Incorporated (price) 12.64
    Product: (id) 1028 (symbol) BSY (companyName) British Sky Broadcasting Group PLC (price) 23.81
~~~





