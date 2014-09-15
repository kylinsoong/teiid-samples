delimiter //
CREATE PROCEDURE simpleproc (
  OUT param1 int
)
BEGIN
  SELECT COUNT(*) INTO param1 FROM employee;
END//
delimiter ;
