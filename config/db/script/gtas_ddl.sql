/*
Navicat MariaDB Data Transfer

Source Server         : local
Source Server Version : 100019
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MariaDB
Target Server Version : 100019
File Encoding         : 65001

Date: 2015-06-08 10:16:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- Dumping database structure for gtas
DROP DATABASE IF EXISTS `gtas`;
CREATE DATABASE IF NOT EXISTS `gtas` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `gtas`;

-- ----------------------------
-- Table structure for gtas_roles
-- ----------------------------
DROP TABLE IF EXISTS `gtas_roles`;
CREATE TABLE `gtas_roles` (
  `role_id` int(11) NOT NULL,
  `role_description` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of gtas_roles
-- ----------------------------
INSERT INTO `gtas_roles` VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `gtas_roles` VALUES ('2', 'ROLE_CUST');

-- ----------------------------
-- Table structure for gtas_users
-- ----------------------------
DROP TABLE IF EXISTS `gtas_users`;
CREATE TABLE `gtas_users` (
  `user_id` varchar(32) NOT NULL,
  `password` varchar(32) DEFAULT NULL,
  `first_name` varchar(32) DEFAULT NULL,
  `last_name` varchar(32) DEFAULT NULL,
  `middle_name` varchar(5) DEFAULT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



--
-- Table structure for table `Airport`
--

DROP TABLE IF EXISTS `Airport`;
CREATE TABLE `Airport` (
  `id` BIGINT(20) NOT NULL,
  `city` VARCHAR(255),
  `countryCode` VARCHAR(255),
  `iata` VARCHAR(255),
  `icao` VARCHAR(255),
  `latitude` VARCHAR(255),
  `name` VARCHAR(255),
  `utcOffset` INTEGER, 
  `insertion_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)

) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


--
-- Table structure for table `Carrier`
--

DROP TABLE IF EXISTS `Carrier`;
CREATE TABLE `Carrier` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255),
   PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


--
-- Table structure for table `Country`
--

DROP TABLE IF EXISTS `Country`;
CREATE TABLE `Country` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `iso2` VARCHAR(255),
  `iso3` VARCHAR(255),
  `iso_numeric` VARCHAR(255),
  `name` VARCHAR(255),
  `upperName` VARCHAR(255),
   PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Table structure for table `Flight`
--

DROP TABLE IF EXISTS `Flight`;
CREATE TABLE `Flight` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_at`DATETIME,
  `created_by` VARCHAR(255),
  `updated_at` DATETIME,
  `updated_by` VARCHAR(255),
  `eta` DATETIME,
  `etd` DATETIME,
  `flightDate` DATETIME,
  `flightNumber` VARCHAR(255),
  `origin` VARCHAR(255),
  `carrier` VARCHAR(255),
  `country` VARCHAR(255),
   PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Table structure for table `Pax`
--

DROP TABLE IF EXISTS `Pax`;
CREATE TABLE `Pax` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `age` INTEGER,
  `debarkation` VARCHAR(255),
  `dob` DATE,  
  `embarkation` VARCHAR(255),
  `firstName` VARCHAR(255),
  `gender` INTEGER,
  `lastName` VARCHAR(255),
  `middleName` VARCHAR(255),
  `suffix` VARCHAR(255),
  `title` VARCHAR(255),
   PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


--
-- Table structure for table `flight_pax`
--

DROP TABLE IF EXISTS `flight_pax`;
CREATE TABLE `flight_pax` (
  `flight_id` BIGINT(20) NOT NULL,
  `pax_id` BIGINT(20) NOT NULL,
   PRIMARY KEY (flight_id, pax_id)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

   ALTER TABLE Flight 
        ADD INDEX FK7D96709072753689 (id), 
        ADD CONSTRAINT FK7D96709072753689 
        FOREIGN KEY (id) 
        REFERENCES Country (id);

    ALTER TABLE Flight 
        ADD INDEX FK7D967090425C454D (id), 
        ADD CONSTRAINT FK7D967090425C454D 
        FOREIGN KEY (id) 
        REFERENCES Carrier (id);

    ALTER TABLE flight_pax 
        ADD INDEX FK7E26AC582721A37 (pax_id), 
        ADD CONSTRAINT FK7E26AC582721A37 
        FOREIGN KEY (pax_id) 
        REFERENCES Pax (id);

    ALTER TABLE flight_pax 
        ADD INDEX FK7E26AC582BE274BD (flight_id), 
        ADD CONSTRAINT FK7E26AC582BE274BD 
        FOREIGN KEY (flight_id) 
        REFERENCES Flight (id);
