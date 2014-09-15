DELIMITER //
DROP PROCEDURE IF EXISTS employee_hos;
CREATE PROCEDURE employee_hos(
  IN con CHAR(20),
  OUT total INT)
BEGIN
  SELECT emp_id, first_name, last_name FROM employee WHERE dept = con;
  SELECT sum(salary) INTO total from employee WHERE dept = con;
END //
DELIMITER ;
