# What's this

This document contain a series commands used to admin & use PostgreSQL Database.

[Legacy Document Entry](https://github.com/kylinsoong/workspace-2014/blob/master/docs/RHL/postgresql_administration.asciidoc)


# Uninstall from Linux

~~~
su -
yum remove postgresql*
rpm -qa | grep postgres*
rm -fr /var/lib/pgsql/
~~~

remove user `postgres`

~~~
userdel -r postgres
rm -fr /var/lib/pgsql/
~~~

> Note, this will remove all postgresql relevant packages from Linux OS.

# Install and Configure the PostgreSQL Database on  Linux

### Install PostgreSQL

Open a terminal window and enter the following commands:

~~~
sudo yum clean all
sudu yum install postgresql-server
~~~

check the installation via command:

~~~
rpm -qa | grep postgres*
~~~

### Set a password for the postgres user

* The installation creates a user named postgres, change the password of User postgres, we only need login as root user enter the following user:

~~~
sudo passwd postgres
~~~

* Enter `redhat` twice for setting the password

> Note, we set `redhat` as password for the postgres user

* Login as postgres by enter the command:

~~~
su - postgres
~~~

then enter password `redhat`

### Initialize the Database

* Open a terminal window, login with the user `postgres`,  Enter the following commands to initialize the database:

~~~
initdb -D /var/lib/pgsql/data
~~~ 

* Start the Database:

~~~
postgres -D /var/lib/pgsql/data
~~~

> This command will hang, just leave the terminal window open. 

* open a new terminal window and switch users to postgres:

~~~
su - postgres
~~~

* Run the psql tool:

~~~
psql
~~~

* Create a password on the database for the `postgres` user:

~~~
ALTER USER postgres PASSWORD 'redhat'
~~~

* exit the psql for entering the command:

~~~
\q
~~~

### Start PostgreSQL as Service

~~~
service postgresql start
service postgresql stop
service postgresql restart
~~~

~~~
chkconfig postgresql on
~~~

# PostgreSQL execute SQL File

The following steps show how to execute SQL File [financials-psql.sql](financials-psql.sql) against running PostgreSQL Database.

### Connect to the PostgreSQL database

~~~
su - postgres
~~~

### Execute SQL File via the following command:

~~~
psql -a -f ~/work/financials-psql.sql
~~~

### View the created database

Start the psql command line utility and type the “\l” to list the databases in PostgreSQL database:

~~~
postgres=# \l
                                    List of databases
     Name      |  Owner   | Encoding |  Collation  |    Ctype    |   Access privileges   
---------------+----------+----------+-------------+-------------+-----------------------
 apaccustomers | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
 brokerinfo    | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
 eucustomers   | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
 postgres      | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
 products      | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
~~~

> Note, `products`, `brokerinfo`, `apaccustomers`, `eucustomers` are database created via [financials-psql.sql](financials-psql.sql)
