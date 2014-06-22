# What's this

This Sample contain a sql scripts for creating database, tables against Mysql database.

# Sql Script

The following SQL Script first create database *brokerinfo*, then create table *Broker* and *Customer*. Each Broker have Id, firstname, lastname and reference with multiple Customers, the Customer use Id for identification.

~~~
DROP DATABASE IF EXISTS brokerinfo;

create DATABASE brokerinfo;

use brokerinfo;

CREATE TABLE Broker (
  Id varchar(15) NOT NULL,
  LastName varchar(25) NOT NULL,
  FirstName varchar(10) NOT NULL,
  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Customer (
  BrokerId varchar(15) DEFAULT NULL,
  Id varchar(15) DEFAULT NULL UNIQUE,
  CONSTRAINT FK_BrokerId_1 FOREIGN KEY (BrokerId) REFERENCES Broker (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
~~~

[Click to download the Completed Script](brokerinfo-mysql.sql). At the end of the script, there are some test data for test convenience.

# JDBC Query

Run [MysqlJDBCClient](../jdbc-client/src/main/java/com/jboss/teiid/mysql/MysqlJDBCClient.java) as Java Application will show how use JDBC Query Mysql database.

> Note, [MysqlJDBCClient](../jdbc-client/src/main/java/com/jboss/teiid/mysql/MysqlJDBCClient.java) have sql statement against mysql database `brokerinfo`, it looks like below:

~~~
UPDATE Broker SET LastName = 'Steve' WHERE Id = 'B1236'
SELECT * FROM Broker
SELECT * FROM Customer
SELECT Broker.Id, Broker.LastName, Broker.FirstName, Customer.ID FROM Broker INNER JOIN Customer ON Broker.Id=Customer.BrokerId
~~~
