
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
  ID varchar(15) DEFAULT NULL,
  CONSTRAINT FK_BrokerId_1 FOREIGN KEY (BrokerId) REFERENCES Broker (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO Broker VALUES ('B1231','Young','Andre'),('B1232','Broadus','Calvin'),('B1233','Townshend','Pete'),('B1234','Daddy','Puff'),('B1235','Carter','Shawn'),('B1236','','Prince'),('B1237','McEnroe','John');

INSERT INTO Customer VALUES ('B1234','CST01002'),('B1235','CST01007'),('B1233','CST01003'),('B1231','CST01006'),('B1232','CST01004'),('B1237','CST01009'),('B1236','CST01010'),('B1232','CST01011'),('B1237','CST01005'),('B1236','CST01012'),('B1236','CST01013'),('B1236','CST01016');
