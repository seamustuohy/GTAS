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


-- ----------------------------
ALTER TABLE ApisMessage
        DROP
        FOREIGN KEY FK_cn3nh4ju2ant6x0mndyl0gbr7;

ALTER TABLE Document
        DROP
        FOREIGN KEY FK_cjb43yenysffpjp0w42bjd73h;

ALTER TABLE flight_pax
        DROP
        FOREIGN KEY FK_nexxsq2ww0msi73x2w7vxlt7e;

ALTER TABLE flight_pax
        DROP
        FOREIGN KEY FK_44f5uuc2sw3lsggy4m7m8f8n2;

ALTER TABLE message_flight
        DROP
        FOREIGN KEY FK_7qu41rq9nwj9kyxsurjiwv2b9;

ALTER TABLE message_flight
        DROP
        FOREIGN KEY FK_n2dqku9mlt3vodtjs2gqb8vm;

DROP TABLE IF EXISTS Airport;
DROP TABLE IF EXISTS ApisMessage;
DROP TABLE IF EXISTS Document;
DROP TABLE IF EXISTS Flight;
DROP TABLE IF EXISTS Message;
DROP TABLE IF EXISTS Pax;
DROP TABLE IF EXISTS ReportingParty;
DROP TABLE IF EXISTS flight_pax;
DROP TABLE IF EXISTS message_flight;

DROP TABLE IF EXISTS RULE;
DROP TABLE IF EXISTS RULE_META;
DROP TABLE IF EXISTS CONDITION;
DROP TABLE IF EXISTS OPERATOR;
DROP TABLE IF EXISTS COND_VALUE;
DROP TABLE IF EXISTS ENTITY_LOOKUP;
DROP TABLE IF EXISTS KNOWLEDGE_BASE;

-- ---------------------------------
-- UDR Tables
-- ---------------------------------
CREATE TABLE RULE (
        ID BIGINT NOT NULL,
        VERSION VARCHAR(255),
        DELETED CHAR(1),
        KB_REF LONG,
        EDITED_BY VARCHAR(32),
        EDIT_DT TIMESTAMP,
        PRIMARY KEY (ID)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE RULE_META (
        ID BIGINT NOT NULL,
        TITLE VARCHAR(64),
        DESCRIPTION VARCHAR(1024),
        START_DT TIMESTAMP,
        END_DT TIMESTAMP,
        ENABLED CHAR(1),
        PRIORITY_HIGH CHAR(1),
        HIT_SHARING CHAR(1),
        PRIMARY KEY (ID)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE RULE_COND (
        ID BIGINT NOT NULL,
        COND_SEQ INTEGER NOT NULL,
        ENTITY_NAME VARCHAR(64) NOT NULL,
        ATTR_NAME VARCHAR(256) NOT NULL,
        OP_CODE VARCHAR(16) NOT NULL,
        PRIMARY KEY (ID, COND_SEQ)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE OPERATOR (
        OP_CODE VARCHAR(16) NOT NULL,
        DESCRIPTION VARCHAR(256),
        PRIMARY KEY (OP_CODE)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE COND_VALUE (
        ID BIGINT NOT NULL,
        COND_SEQ INTEGER NOT NULL,
        VAL_NAME VARCHAR(64) NOT NULL,
        VAL_TYPE VARCHAR(32) NOT NULL,
        NUM_VAL NUMERIC,
        DT_VAL DATE,
        CHAR_VAL VARCHAR(2048),
        PRIMARY KEY (ID, COND_SEQ, VAL_NAME)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE ENTITY_LOOKUP (
        ENTITY_NAME VARCHAR(64) NOT NULL,
        DESCRIPTION VARCHAR(256),
        PRIMARY KEY (ENTITY_NAME)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE KNOWLEDGE_BASE (
        ID BIGINT NOT NULL,
        VERSION VARCHAR(256),
        KB_BLOB BLOB,
        CREATION_DT TIMESTAMP,
        PRIMARY KEY (ID)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Table structure for table `Airport`
--

CREATE TABLE Airport (
        id VARCHAR(255) NOT NULL,
        city VARCHAR(255),
        countryCode VARCHAR(255),
        iata VARCHAR(255),
        icao VARCHAR(255),
        latitude VARCHAR(255),
        longitude VARCHAR(255),
        NAME VARCHAR(255),
        timezone VARCHAR(255),
        utcOffset INTEGER,
        PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Table structure for table `ApisMessage`
--
    CREATE TABLE ApisMessage (
        transmissionDate DATETIME,
        transmissionSource VARCHAR(255),
        id BIGINT UNSIGNED NOT NULL,
        PRIMARY KEY (id)
    ) ENGINE=INNODB;

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
    CREATE TABLE Flight (
        id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
        created_at DATETIME,
        created_by VARCHAR(20),
        updated_at DATETIME,
        updated_by VARCHAR(20),
        carrier VARCHAR(255),
        destination VARCHAR(255),
        destinationCountry VARCHAR(255),
        eta DATETIME,
        etd DATETIME,
        flightDate DATETIME,
        flightNumber VARCHAR(255),
        origin VARCHAR(255),
        originCountry VARCHAR(255),
        PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Table structure for table `Pax`
--

DROP TABLE IF EXISTS `Pax`;
    CREATE TABLE Pax (
        id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
        age INTEGER,
        debarkation VARCHAR(255),
        dob DATE,
        embarkation VARCHAR(255),
        firstName VARCHAR(255),
        gender VARCHAR(255),
        lastName VARCHAR(255),
        middleName VARCHAR(255),
        suffix VARCHAR(255),
        title VARCHAR(255),
        TYPE VARCHAR(255),
        PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


--
-- Table structure for table `flight_pax`
--

DROP TABLE IF EXISTS `flight_pax`;
    CREATE TABLE flight_pax (
        flight_id BIGINT UNSIGNED NOT NULL,
        pax_id BIGINT UNSIGNED NOT NULL,
        PRIMARY KEY (flight_id, pax_id)
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


    CREATE TABLE Document (
        id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
        documentType VARCHAR(255),
        expirationDate DATETIME,
        issuanceCountry VARCHAR(255),
        issuanceDate DATETIME,
        number VARCHAR(255),
        pax_id BIGINT UNSIGNED,
        PRIMARY KEY (id)
    ) ENGINE=INNODB;


    CREATE TABLE Message (
        id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
        createDate DATETIME,
        hashCode VARCHAR(255),
        raw LONGBLOB,
        PRIMARY KEY (id)
    ) ENGINE=INNODB;


    CREATE TABLE ReportingParty (
        id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
        fax VARCHAR(255),
        partyName VARCHAR(255),
        telephone VARCHAR(255),
        PRIMARY KEY (id)
    ) ENGINE=INNODB;

    CREATE TABLE message_flight (
        flight_id BIGINT UNSIGNED NOT NULL,
        message_id BIGINT UNSIGNED NOT NULL,
        PRIMARY KEY (flight_id, message_id)
    ) ENGINE=INNODB;

    ALTER TABLE ApisMessage
        ADD CONSTRAINT FK_cn3nh4ju2ant6x0mndyl0gbr7
        FOREIGN KEY (id)
        REFERENCES Message (id);

    ALTER TABLE Document
        ADD CONSTRAINT FK_cjb43yenysffpjp0w42bjd73h
        FOREIGN KEY (pax_id)
        REFERENCES Pax (id);

    ALTER TABLE flight_pax
        ADD CONSTRAINT FK_nexxsq2ww0msi73x2w7vxlt7e
        FOREIGN KEY (pax_id)
        REFERENCES Pax (id);

    ALTER TABLE flight_pax
        ADD CONSTRAINT FK_44f5uuc2sw3lsggy4m7m8f8n2
        FOREIGN KEY (flight_id)
        REFERENCES Flight (id);

    ALTER TABLE message_flight
        ADD CONSTRAINT FK_7qu41rq9nwj9kyxsurjiwv2b9
        FOREIGN KEY (message_id)
        REFERENCES Flight (id);

    ALTER TABLE message_flight
        ADD CONSTRAINT FK_n2dqku9mlt3vodtjs2gqb8vm
        FOREIGN KEY (flight_id)
        REFERENCES ApisMessage (id);
    


--   ALTER TABLE Flight 
--        ADD INDEX FK7D96709072753689 (id), 
--        ADD CONSTRAINT FK7D96709072753689 
--        FOREIGN KEY (id) 
--        REFERENCES Country (id);

--    ALTER TABLE Flight 
--        ADD INDEX FK7D967090425C454D (id), 
--        ADD CONSTRAINT FK7D967090425C454D 
--        FOREIGN KEY (id) 
--        REFERENCES Carrier (id);

--    ALTER TABLE flight_pax 
--        ADD INDEX FK7E26AC582721A37 (pax_id), 
--        ADD CONSTRAINT FK7E26AC582721A37 
--        FOREIGN KEY (pax_id) 
--        REFERENCES Pax (id);

--    ALTER TABLE flight_pax 
--        ADD INDEX FK7E26AC582BE274BD (flight_id), 
--        ADD CONSTRAINT FK7E26AC582BE274BD 
--        FOREIGN KEY (flight_id) 
--        REFERENCES Flight (id);
