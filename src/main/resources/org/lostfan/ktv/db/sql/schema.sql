CREATE TABLE `SUBSCRIBER` (
  `ID` VARCHAR(50) NOT NULL
  , `NAME` VARCHAR(50) NOT NULL
  , `BALANCE` INT
);

CREATE TABLE `SERVICE` (
  `NAME` VARCHAR(50) NOT NULL
  , `ADDIIONALSERVICE` BIT
);

CREATE TABLE `MATERIAL` (
  `NAME` VARCHAR(50) NOT NULL
  , `PRICE` INT
);

CREATE TABLE `TARIFF` (
  `NAME` VARCHAR(50) NOT NULL
  , `CHANNELS` VARCHAR(50)
);

CREATE TABLE SUBSCRIBER (
  ID VARCHAR(20) NOT NULL
  , NAME VARCHAR(50) NOT NULL
  , BALANCE INT,
 CONNECTED BIT
);
