-- root 계정으로 실행
-- user1 계정을 자동 생성하고 권한을 부여함

DROP USER IF EXISTS 'user1'@'localhost';

CREATE USER 'user1'@'localhost'
IDENTIFIED BY '1234';

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema DBTEST
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema DBTEST
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `DBTEST` DEFAULT CHARACTER SET utf8 ;
USE `DBTEST` ;

-- -----------------------------------------------------
-- Table `DBTEST`.`employee`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DBTEST`.`employee` ;

CREATE TABLE IF NOT EXISTS `DBTEST`.`employee` (
  `emp_id` INT NOT NULL AUTO_INCREMENT,
  `emp_name` VARCHAR(20) NOT NULL,
  `emp_role` ENUM('ADMIN', 'SALESMAN') NOT NULL DEFAULT 'SALESMAN',
  `account_id` VARCHAR(20) NOT NULL,
  `account_pw` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`emp_id`),
  UNIQUE INDEX `account_id_UNIQUE` (`account_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DBTEST`.`customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DBTEST`.`customer` ;

CREATE TABLE IF NOT EXISTS `DBTEST`.`customer` (
  `cust_id` INT NOT NULL AUTO_INCREMENT,
  `cust_name` VARCHAR(20) NOT NULL,
  `cust_phone` VARCHAR(15) NOT NULL,
  `cust_email` VARCHAR(50) NULL,
  `employee_emp_id` INT NOT NULL,
  PRIMARY KEY (`cust_id`),
  UNIQUE INDEX `cust_email_UNIQUE` (`cust_email` ASC) VISIBLE,
  INDEX `fk_customer_employee1_idx` (`employee_emp_id` ASC) VISIBLE,
  CONSTRAINT `fk_customer_employee1`
    FOREIGN KEY (`employee_emp_id`)
    REFERENCES `DBTEST`.`employee` (`emp_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DBTEST`.`car_series`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DBTEST`.`car_series` ;

CREATE TABLE IF NOT EXISTS `DBTEST`.`car_series` (
  `series_id` INT NOT NULL AUTO_INCREMENT,
  `series_name` VARCHAR(20) NOT NULL,
  `car_type` ENUM('스포츠카', '세단', '전기차', 'SUV') NOT NULL,
  `powertrain_type` ENUM('디젤', '가솔린', '하이브리드', '전기차') NOT NULL,
  UNIQUE INDEX `series_name_UNIQUE` (`series_name` ASC) VISIBLE,
  PRIMARY KEY (`series_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DBTEST`.`series_model`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DBTEST`.`series_model` ;

CREATE TABLE IF NOT EXISTS `DBTEST`.`series_model` (
  `model_id` INT NOT NULL AUTO_INCREMENT,
  `trim_name` VARCHAR(30) NOT NULL,
  `launch_year` INT NOT NULL,
  `base_price` INT NOT NULL,
  `car_series_series_id` INT NOT NULL,
  PRIMARY KEY (`model_id`),
  INDEX `fk_series_model_car_series1_idx` (`car_series_series_id` ASC) VISIBLE,
  CONSTRAINT `fk_series_model_car_series1`
    FOREIGN KEY (`car_series_series_id`)
    REFERENCES `DBTEST`.`car_series` (`series_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DBTEST`.`inventory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DBTEST`.`inventory` ;

CREATE TABLE IF NOT EXISTS `DBTEST`.`inventory` (
  `car_id` INT NOT NULL,
  `car_color` VARCHAR(20) NOT NULL,
  `import_price` INT NOT NULL,
  `sale_price` INT NOT NULL,
  `status` ENUM('판매가능', '판매완료') NOT NULL DEFAULT '판매가능',
  `series_model_model_id` INT NOT NULL,
  PRIMARY KEY (`car_id`),
  INDEX `fk_inventory_series_model1_idx` (`series_model_model_id` ASC) VISIBLE,
  CONSTRAINT `fk_inventory_series_model1`
    FOREIGN KEY (`series_model_model_id`)
    REFERENCES `DBTEST`.`series_model` (`model_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DBTEST`.`sale_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DBTEST`.`sale_history` ;

CREATE TABLE IF NOT EXISTS `DBTEST`.`sale_history` (
  `sale_id` INT NOT NULL AUTO_INCREMENT,
  `sale_date` DATE NOT NULL,
  `customer_cust_id` INT NOT NULL,
  `employee_emp_id` INT NOT NULL,
  `inventory_car_id` INT NOT NULL,
  PRIMARY KEY (`sale_id`),
  UNIQUE INDEX `inventory_car_id_UNIQUE` (`inventory_car_id` ASC) VISIBLE,
  INDEX `fk_sale_history_customer1_idx` (`customer_cust_id` ASC) VISIBLE,
  INDEX `fk_sale_history_employee1_idx` (`employee_emp_id` ASC) VISIBLE,
  INDEX `fk_sale_history_inventory1_idx` (`inventory_car_id` ASC) VISIBLE,
  CONSTRAINT `fk_sale_history_customer1`
    FOREIGN KEY (`customer_cust_id`)
    REFERENCES `DBTEST`.`customer` (`cust_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sale_history_employee1`
    FOREIGN KEY (`employee_emp_id`)
    REFERENCES `DBTEST`.`employee` (`emp_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sale_history_inventory1`
    FOREIGN KEY (`inventory_car_id`)
    REFERENCES `DBTEST`.`inventory` (`car_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DBTEST`.`import`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DBTEST`.`import` ;

CREATE TABLE IF NOT EXISTS `DBTEST`.`import` (
  `car_id` INT NOT NULL AUTO_INCREMENT,
  `import_date` DATE NOT NULL,
  `import_price` INT NOT NULL,
  `car_color` VARCHAR(20) NOT NULL,
  `series_model_model_id` INT NOT NULL,
  PRIMARY KEY (`car_id`),
  INDEX `fk_import_series_model1_idx` (`series_model_model_id` ASC) VISIBLE,
  CONSTRAINT `fk_import_series_model1`
    FOREIGN KEY (`series_model_model_id`)
    REFERENCES `DBTEST`.`series_model` (`model_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO car_series(series_name, car_type, powertrain_type)
VALUES
('911', '스포츠카', '가솔린'),
('718 Boxster', '스포츠카', '가솔린'),
('718 Cayman', '스포츠카', '가솔린'),
('Panamera', '세단', '가솔린'),
('Cayenne', 'SUV', '가솔린'),
('Macan', 'SUV', '가솔린'),
('Taycan', '전기차', '전기차'),
('Macan electric', '전기차', '전기차');

INSERT INTO series_model(car_series_series_id, trim_name, launch_year, base_price)
VALUES
(1, 'Carrera GTS', 2026, 240700000),
(1, 'Turbo S', 2026, 342700000),
(2, 'GTS 4.0', 2025, 135300000),
(2, 'Base', 2026, 120000000),
(3, 'GTS 4.0', 2025, 130500000),
(3, 'Base', 2026, 100000000),
(4, '4', 2027, 184700000),
(4, 'GTS', 2027, 254200000),
(5, 'Base', 2026, 143800000),
(5, 'GTS', 2026, 199400000),
(6, 'T', 2026, 100000000),
(6, 'S', 2026, 110000000),
(7, 'GTS', 2026, 179900000),
(7, 'Turbo', 2026, 216100000),
(8, '4', 2026, 109000000),
(8, 'Turbo', 2026, 142300000);

INSERT INTO employee(emp_name, emp_role, account_id, account_pw)
VALUES
('차범근', 'ADMIN', 'admin', '1234'),
('박지성', 'SALESMAN', 'sales1', '1234'),
('손흥민', 'SALESMAN', 'sales2', '1234'),
('이강인', 'SALESMAN', 'sales3', '1234'),
('김민재', 'SALESMAN', 'sales4', '1234');

INSERT INTO import(series_model_model_id, import_date, import_price, car_color)
VALUES
(10, '2025-01-08', 195000000, 'Black'),
(3, '2025-01-14', 130000000, 'Silver'),
(14, '2025-01-22', 210000000, 'White'),
(1, '2025-02-05', 235000000, 'Grey'),
(7, '2025-02-12', 184000000, 'Black'),
(12, '2025-02-19', 105000000, 'Grey'),
(5, '2025-02-27', 128000000, 'Blue'),
(16, '2025-03-04', 141000000, 'Grey'),
(2, '2025-03-11', 335000000, 'Black'),
(9, '2025-03-20', 142000000, 'White'),
(4, '2025-03-28', 115000000, 'Blue'),
(13, '2025-04-02', 179000000, 'Red'),
(6, '2025-04-09', 95000000, 'Grey'),
(11, '2025-04-17', 98000000, 'Red'),
(8, '2025-04-25', 250000000, 'Blue'),
(15, '2025-05-06', 108000000, 'White'),
(2, '2025-05-14', 340000000, 'Black'),
(5, '2025-05-22', 125000000, 'Red'),
(10, '2025-05-30', 198000000, 'Black'),
(4, '2025-06-03', 118000000, 'White'),
(13, '2025-06-11', 178000000, 'Silver'),
(1, '2025-06-19', 230000000, 'Black'),
(6, '2025-06-27', 99000000, 'White'),
(7, '2025-07-05', 180000000, 'Black'),
(14, '2025-07-12', 215000000, 'Black'),
(3, '2025-07-20', 132000000, 'Red'),
(12, '2025-07-28', 109000000, 'Grey'),
(9, '2025-08-04', 143000000, 'Silver'),
(11, '2025-08-12', 95000000, 'Yellow'),
(8, '2025-08-20', 245000000, 'Grey'),
(15, '2025-08-28', 105000000, 'Blue'),
(1, '2025-09-05', 238000000, 'Blue'),
(4, '2025-09-13', 119000000, 'Silver'),
(16, '2025-09-21', 140000000, 'Silver'),
(10, '2025-09-29', 190000000, 'Silver'),
(6, '2025-10-06', 98000000, 'Silver'),
(2, '2025-10-14', 330000000, 'White'),
(8, '2025-10-22', 240000000, 'White'),
(13, '2025-10-30', 175000000, 'Black'),
(3, '2025-11-07', 133000000, 'Yellow'),
(11, '2025-11-15', 99000000, 'White'),
(9, '2025-11-23', 140000000, 'Blue'),
(5, '2025-12-01', 129000000, 'Red'),
(7, '2025-12-18', 182000000, 'Black'),
(12, '2025-12-26', 108000000, 'Blue'),
(1, '2026-01-01', 210000000, 'Black'),
(1, '2026-01-12', 215000000, 'Crayon Grey'),
(1, '2026-01-22', 208000000, 'Carrera White'),
(2, '2026-02-05', 310000000, 'GT Silver'),
(2, '2026-02-13', 298000000, 'Jet Black'),
(2, '2026-02-28', 305000000, 'Guards Red'),
(3, '2026-03-03', 118000000, 'Python Green'),
(3, '2026-03-15', 120000000, 'White'),
(4, '2026-03-26', 105000000, 'Gentian Blue'),
(4, '2026-04-04', 102000000, 'Black'),
(5, '2026-04-19', 115000000, 'Shark Blue'),
(5, '2026-04-25', 112000000, 'Chalk'),
(6, '2026-05-05', 88000000, 'White'),
(6, '2026-05-08', 89000000, 'Jet Black'),
(7, '2026-05-15', 165000000, 'Ice Grey Metallic'),
(7, '2026-06-06', 162000000, 'Carrera White'),
(8, '2026-06-14', 228000000, 'Carmine Red'),
(8, '2026-06-19', 231000000, 'Black'),
(9, '2026-06-22', 128000000, 'GT Silver'),
(9, '2026-07-07', 130000000, 'Dolomite Silver'),
(10, '2026-07-15', 178000000, 'Crayon Grey'),
(10, '2026-07-22', 175000000, 'Jet Black'),
(10, '2026-07-31', 180000000, 'Guards Red'),
(11, '2026-08-08', 87000000, 'White'),
(11, '2026-08-11', 89000000, 'Gentian Blue'),
(12, '2026-08-28', 96000000, 'Black'),
(12, '2026-09-09', 98000000, 'Chalk'),
(13, '2026-09-18', 160000000, 'Carmine Red'),
(13, '2026-09-22', 158000000, 'Carrera White'),
(13, '2026-10-03', 162000000, 'Jet Black'),
(14, '2026-10-10', 192000000, 'GT Silver'),
(14, '2026-10-21', 195000000, 'Crayon Grey'),
(15, '2026-11-11', 95000000, 'White'),
(15, '2026-11-18', 97000000, 'Black'),
(16, '2026-11-22', 125000000, 'Jet Black'),
(16, '2026-11-29', 128000000, 'Ice Grey Metallic');

INSERT INTO customer(cust_name, cust_phone, cust_email, employee_emp_id)
VALUES
('이지은', '010-0516-0918', 'jieun@test.com', 2),
('김민지', '010-0507-0722', 'minji@test.com', 2),
('김채원', '010-0801-1029', 'chaewon@test.com', 2),
('정원이', '010-0525-0326', 'woni@test.com', 3),
('정현우', '010-5555-5555', 'hyunwoo@test.com', 3),
('한지민', '010-1395-1293', 'jimin@test.com', 3),
('박보영', '010-6666-6666', 'boyoung@test.com', 4),
('오세훈', '010-7777-7777', 'sehun@test.com', 4),
('윤아름', '010-8888-8888', 'areum@test.com', 4),
('강도윤', '010-9999-9999', 'doyoon@test.com', 5),
('송하린', '010-1212-1212', 'harin@test.com', 5);

INSERT INTO inventory (car_id, series_model_model_id, import_price, sale_price, car_color, status)
VALUES
(1, 10, 195000000, 210000000, 'Black', '판매완료'),
(2, 3, 130000000, 140000000, 'Silver', '판매가능'),
(3, 14, 210000000, 220000000, 'White', '판매가능'),
(4, 1, 235000000, 248000000, 'Grey', '판매가능'),
(5, 7, 184000000, 200000000, 'Black', '판매완료'),
(6, 12, 105000000, 115000000, 'Grey', '판매가능'),
(7, 5, 128000000, 138000000, 'Blue', '판매가능'),
(8, 16, 141000000, 150000000, 'Grey', '판매완료'),
(9, 2, 335000000, 355000000, 'Black', '판매가능'),
(10, 9, 142000000, 155000000, 'White', '판매가능'),
(11, 4, 115000000, 125000000, 'Blue', '판매가능'),
(12, 13, 179000000, 195000000, 'Red', '판매완료'),
(13, 6, 95000000, 105000000, 'Grey', '판매가능'),
(14, 11, 98000000, 110000000, 'Red', '판매완료'),
(15, 8, 250000000, 270000000, 'Blue', '판매완료'),
(16, 15, 108000000, 120000000, 'White', '판매완료'),
(17, 2, 340000000, 360000000, 'Black', '판매완료'),
(18, 5, 125000000, 135000000, 'Red', '판매가능'),
(19, 10, 198000000, 215000000, 'Black', '판매완료'),
(20, 4, 118000000, 128000000, 'White', '판매가능'),
(21, 13, 178000000, 190000000, 'Silver', '판매완료'),
(22, 1, 230000000, 245000000, 'Black', '판매가능'),
(23, 6, 99000000, 110000000, 'White', '판매완료'),
(24, 7, 180000000, 190000000, 'Black', '판매가능'),
(25, 14, 215000000, 230000000, 'Black', '판매완료'),
(26, 3, 132000000, 142000000, 'Red', '판매가능'),
(27, 12, 109000000, 120000000, 'Grey', '판매완료'),
(28, 9, 143000000, 155000000, 'Silver', '판매완료'),
(29, 11, 95000000, 105000000, 'Yellow', '판매가능'),
(30, 8, 245000000, 265000000, 'Grey', '판매가능'),
(31, 15, 105000000, 115000000, 'Blue', '판매가능'),
(32, 1, 238000000, 250000000, 'Blue', '판매완료'),
(33, 4, 119000000, 130000000, 'Silver', '판매완료'),
(34, 16, 140000000, 145000000, 'Silver', '판매가능'),
(35, 10, 190000000, 205000000, 'Silver', '판매가능'),
(36, 6, 98000000, 108000000, 'Silver', '판매가능'),
(37, 2, 330000000, 350000000, 'White', '판매가능'),
(38, 8, 240000000, 260000000, 'White', '판매가능'),
(39, 13, 175000000, 185000000, 'Black', '판매가능'),
(40, 3, 133000000, 145000000, 'Yellow', '판매완료'),
(41, 11, 99000000, 110000000, 'White', '판매완료'),
(42, 9, 140000000, 150000000, 'Blue', '판매가능'),
(43, 5, 129000000, 140000000, 'Red', '판매완료'),
(44, 7, 182000000, 195000000, 'Black', '판매가능'),
(45, 12, 108000000, 120000000, 'Blue', '판매완료');

INSERT INTO sale_history (inventory_car_id, customer_cust_id, employee_emp_id, sale_date)
VALUES
(15, 2, 2, '2026-05-01'),
(43, 4, 3, '2026-05-02'),
(1, 1, 2, '2026-05-03'),
(23, 3, 2, '2026-05-04'),
(17, 8, 4, '2026-05-05'),
(41, 9, 4, '2026-05-06'),
(5, 4, 3, '2026-05-07'),
(28, 6, 3, '2026-05-08'),
(12, 10, 5, '2026-05-09'),
(19, 11, 5, '2026-05-10'),
(27, 3, 2, '2026-05-11'),
(8, 7, 4, '2026-05-12'),
(33, 5, 3, '2026-05-13'),
(14, 1, 2, '2026-05-14'),
(45, 9, 4, '2026-05-15'),
(40, 8, 4, '2026-05-16'),
(21, 2, 2, '2026-05-17'),
(25, 6, 3, '2026-05-18'),
(16, 5, 3, '2026-05-19'),
(32, 7, 4, '2026-05-20');


GRANT SELECT ON DBTEST.customer TO 'user1'@'localhost';
GRANT SELECT ON DBTEST.employee TO 'user1'@'localhost';
GRANT SELECT ON DBTEST.inventory TO 'user1'@'localhost';
GRANT SELECT ON DBTEST.sale_history TO 'user1'@'localhost';
GRANT SELECT ON DBTEST.car_series TO 'user1'@'localhost';
GRANT SELECT ON DBTEST.series_model TO 'user1'@'localhost';

GRANT INSERT ON DBTEST.customer TO 'user1'@'localhost';
GRANT INSERT ON DBTEST.sale_history TO 'user1'@'localhost';

GRANT UPDATE ON DBTEST.customer TO 'user1'@'localhost';
GRANT UPDATE ON DBTEST.inventory TO 'user1'@'localhost';

FLUSH PRIVILEGES;