# User Accounts

The following account types are used in JBoss Data Virtualization:

*JBoss EAP Management User* - A JBoss EAP Management User is required to administer JBoss Data Virtualization via the Management Console, Management CLI, JBoss Developer Studio, and Admin API.

*JBoss Data Virtualization User* - A JBoss Data Virtualization user identifies valid users and their roles in relation to accessing virtual databases (VDBs).

*Hierarchical Database User* - A hierarchical database user identifies valid users and their roles in relation to accessing the provided hierarchical database.

### Data Roles

For the purpose of authorizing users for accessing virtual databases (VDBs), data roles are defined in *MET A-INF/vdb.xml* within the VDB file.

As part of the data role definition they can be mapped to JAAS roles which are specified within <mapped-role-name> tags. T hese mappings can be established using the addDataRoleMapping() method.

How these JAAS roles are associated with users depends on the particular JAAS login module used. For instance, the default UsersRolesLoginModule associates users with JAAS roles in plaintext files.

### Adding a JBoss EAP Management User

Use `add-user.sh` (`add-user.bat` in Windows) to create JBoss EAP Management User, the following is quick way to create User:

~~~
[user@ host bi n]$ ./add-user.sh username password
~~~

To use the application realm, use the -a parameter:

~~~
[user@ host bi n]$ ./add-user.sh -a username password
~~~

# Adding a JBoss Data Virtualization User

The default configuration for JBoss Data Virtualization uses plain text files to define users and their roles. The following procedure explains how to add a user and their roles for this configuration.

### Add a username and password

* Open the `EAP_HOME/MODE/configuration/teiid-security-users.properties` file in a text editor

* Add the required username and password as a new line with the syntax: username=password. For example:

~~~
user=user
kylin=password1!
~~~

* Save and close the file

### Assign user roles

* Open the `EAP_HOME/MODE/configuration/teiid-security-roles.properties` file in a text editor

* Add the user and assigned roles to the file as a new line with the syntax: username=role1,role2,... Any number of roles can be assigned. If no roles are assigned to the user then no entry is required. For example:

~~~
user=odata,example-role
kylin=user,admin
~~~

* Save and close the file

# Adding an Hierarchical Database User

The default configuration uses plain text files to define users and their roles. The following procedure explains how to add a user and their roles for this configuration.

### Add a username and password

* Open the `EAP_HOME/MODE/configuration/modeshape-users.properties` file in a text editor

* Add the required username and password as a new line with the syntax: username=password. For example:

~~~
test=password1!
~~~

* Save and close the file

### Assign user roles

* Open the `EAP_HOME/MODE/configuration/modeshape-roles.properties` file in a text editor

* Add the user and assigned roles to the file as a new line with the syntax: username=role1,role2,.... Any number of roles can be assigned. If no roles are assigned to the user then no entry is required. For example:

~~~
administrator=administrator
test=accounts,inventory,reports
~~~

* Save and close the file


# Management Console

Log in to the Management Console via [http://localhost:9990/console](http://localhost:9990/console)

# Management CLI

Launch the CLI in Linux via execute `EAP_HOME/bin/jboss-cli.sh`.

# AdminShell

* AdminShell provides the following features:

*Administration* - AdminShell can be used to connect to a JBoss Data Virtualization instance in order to perform various administrative tasks. 

*Data Access* - AdminShell can be used to connect to a virtual database (VDB) and run SQL commands to query VDB data and view results.

*Migration* - AdminShell can be used to develop scripts that will move VDBs and associated components from one development environment to another. (Users can test and automate migration scripts before executing them in production deployments.)

*Testing* - The built-in JUnit Test Framework allows users to write regression tests to check system health and data integrity. The written tests validate system functionality automatically, removing the need for manual verification by QA Personnel.

* AdminShell provides a script-based command language for users to administer JBoss Data Virtualization. AdminShell is provided by the following interfaces:

*Interactive AdminShell* - The interactive AdminShell provides a command-line interface in which users can issue ad hoc script based commands for simple administration of JBoss Data Virtualization.

*Non-interacive AdminShell* - The non-interactive AdminShell allows users to administer JBoss Data Virtualization by executing previously developed scripts from the command-line. This mode is especially useful to automate testing and to perform repeated configuration/migration changes to a JBoss Data Virtualization instance.

*AdminShell GUI* - The AdminShell GUI enables users to administer JBoss Data Virtualization through the development and running of scripts in a graphical environment. Script development is done using a GUI text editor with syntax highlighting.

[More about Groovy](http://groovy.codehaus.org/)

### Launch AdminShell

Navigate to `dataVirtualization/teiid-adminshell`, execute:

~~~
./adminshell.sh
~~~

Alternatively, launch the AdminShell GUI via:

~~~
./adminshell-console.sh
~~~

### Save a Script in Interactive AdminShell

~~~
record start /home/kylin/tmp/scripts
...
record stop
~~~

> Note that this will generate a series commands in /home/kylin/tmp/scripts, use ./adminshell.sh /home/kylin/tmp/scripts can execute these commands continuely.

### Example Scripts

In [Mysql Database as Data Source](jdv-mysqldatasource.md), we have created VDB `BrokerInfo_VDB.vdb`, In this examples, we first deploy `BrokerInfo_VDB.vdb` to JDV, then query Database against VDB, Note deploy need admin connection, so the scripts as:

~~~
connectAsAdmin();
deploy("/home/kylin/work/project/teiid-samples/workspace/MysqlDataSource/BrokerInfo_VDB.vdb");
disconnect();
conn = connect("jdbc:teiid:BrokerInfo_VDB@mm://localhost:31000;version=1","user","user");
conn.eachRow("SELECT * FROM BrokerInfo_VBL.Broker"){println "${it}"}
conn.eachRow("SELECT * FROM BrokerInfo_VBL.Customer"){println "${it}"}
disconnect();
exit;
~~~ 
