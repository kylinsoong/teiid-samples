# What's this

This Document contain a series commands used to admin & use mysql.

[Legacy Document Entry](https://github.com/kylinsoong/workspace-2014/blob/master/docs/RHL/mysql_administration.asciidoc)

# Create DataBase, Tables and Insert Data

*CREATE DATABASE* - use sql commands to create database usually need to login as mysql root user account, the `CREATE DATABASE` statement need the CREATE privilege for the database.

*CREATE TABLE* - the `CREATE TABLE` statement need  the CREATE privilege for the table.

### Procedure for creating a database and a sample table

Login as the mysql root user to create database:

~~~
mysql -u root -p
~~~

Sample outputs:

~~~
mysql>
~~~

Add a database called books, enter:

~~~
CREATE DATABASE books;
~~~

Now, database is created. Use a database with use command, type:

~~~
USE books;
~~~

Next, create a table called authors with name, email and id as fields:

~~~
CREATE TABLE authors (id INT, name VARCHAR(20), email VARCHAR(20));
~~~

To display your tables in books database, enter:

~~~
SHOW TABLES;
~~~

# User Create & Privilege Grant

If the user already exist, `CREATE USER` cause a error throw. `CREATE USER` must have the global *CREATE USER* privilege or the *INSERT* privilege for the mysql database.

The following is sample for create/drop user `jdv_user` and grant privilege.

~~~
CREATE USER 'jdv_user'@'localhost' IDENTIFIED BY 'jdv_pass';
CREATE USER 'jdv_user'@'kylin.redhat.com' IDENTIFIED BY 'jdv_pass';
drop user 'jdv_user'@'localhost';
drop user 'jdv_user'@'kylin.redhat.com';
~~~

Assume we have database `TESTDB`, the grant privilege statement can be:

~~~
GRANT ALL ON TESTDB.* TO 'jdv_user'@'localhost';
GRANT SELECT ON TESTDB.* TO 'jdv_user'@'localhost';
GRANT USAGE ON *.* TO 'jdv_user'@'localhost';
~~~

For simplify, we can use the following grant all privileges tp user:

~~~
GRANT ALL ON *.* TO 'jdv_user'@'localhost';
GRANT ALL ON *.* TO 'jdv_user'@'kylin.redhat.com';
~~~


