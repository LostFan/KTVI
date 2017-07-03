CREATE SEQUENCE serial_street START 1;

CREATE TABLE "street" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_street'),
  "name" VARCHAR(100) NOT NULL
);

CREATE SEQUENCE serial_material START 1;

CREATE TABLE "material" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_material'),
  "name" VARCHAR(100) NOT NULL,
  "price" INTEGER DEFAULT 0,
  "unit" VARCHAR(10)
);

CREATE SEQUENCE serial_equipment START 1;

CREATE TABLE "equipment" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_equipment'),
  "name" VARCHAR(100) NOT NULL,
  "price" INTEGER DEFAULT 0
);

CREATE SEQUENCE serial_service START 1;

CREATE TABLE "service" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_service'),
  "name" VARCHAR(100) NOT NULL,
  "additional" BOOLEAN DEFAULT TRUE,
  "consume_materials" BOOLEAN DEFAULT FALSE,
  "change_tariff" BOOLEAN DEFAULT FALSE,
  "connection_service" BOOLEAN DEFAULT FALSE,
  "disconnection_service" BOOLEAN DEFAULT FALSE
);

CREATE TABLE "service_price" (
  "service_id" INTEGER,
  "date" DATE,
  "price" INTEGER DEFAULT 0,
  PRIMARY KEY("service_id", "date"),
  FOREIGN KEY ("service_id") REFERENCES "service"("id")
);

CREATE SEQUENCE serial_tariff START 1;

CREATE TABLE "tariff" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_tariff'),
  "name" VARCHAR(100) NOT NULL
);

CREATE TABLE "tariff_price" (
  "tariff_id" INTEGER,
  "date" DATE,
  "price" INTEGER DEFAULT 0,
  PRIMARY KEY("tariff_id", "date"),
  FOREIGN KEY ("tariff_id") REFERENCES "tariff"("id")
);

CREATE SEQUENCE serial_disconnection_reason START 1;

CREATE TABLE "disconnection_reason" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_disconnection_reason'),
  "name" VARCHAR(100) NOT NULL
);

CREATE TABLE "subscriber" (
  "account" INTEGER PRIMARY KEY,
  "name" VARCHAR(64) NOT NULL,
  "balance" NUMERIC DEFAULT 0 NOT NULL,
  "connected" BOOLEAN DEFAULT FALSE,
  "street_id" INTEGER,
  "house" INTEGER,
  "index" VARCHAR(16) DEFAULT '',
  "building" VARCHAR(16) DEFAULT '',
  "flat" VARCHAR(16) DEFAULT '',
  "phone" VARCHAR(64) DEFAULT '',
  "passport_number" VARCHAR(16) DEFAULT '',
  "passport_authority" VARCHAR(128) DEFAULT '',
  "passport_date" DATE DEFAULT NULL,
  "date_of_contract" DATE DEFAULT NULL,
  "information" VARCHAR(1000) DEFAULT '',
  FOREIGN KEY ("street_id") REFERENCES "street"("id")
);

CREATE TABLE "subscriber_session" (
  "subscriber_account" INTEGER NOT NULL,
  "connection_date" DATE NOT NULL,
  "disconnection_date" DATE DEFAULT NULL,
  "disconnection_reason_id" INTEGER DEFAULT NULL,
  PRIMARY KEY("subscriber_account", "connection_date"),
  FOREIGN KEY ("subscriber_account") REFERENCES "subscriber"("account"),
  FOREIGN KEY ("disconnection_reason_id") REFERENCES "disconnection_reason"("id")
);

CREATE TABLE "subscriber_tariff" (
  "subscriber_account" INTEGER NOT NULL,
  "tariff_id" INTEGER NOT NULL,
  "connection_date" DATE NOT NULL,
  "disconnection_date" DATE DEFAULT NULL,
  PRIMARY KEY("subscriber_account", "connection_date"),
  FOREIGN KEY ("subscriber_account") REFERENCES "subscriber"("account"),
  FOREIGN KEY ("tariff_id") REFERENCES "tariff"("id")
);

CREATE SEQUENCE serial_rendered_service START 1;

CREATE TABLE "rendered_service" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_rendered_service'),
  "subscriber_account" INTEGER NOT NULL,
  "service_id" INTEGER NOT NULL,
  "date" DATE NOT NULL,
  "price" NUMERIC DEFAULT 0,
  FOREIGN KEY ("subscriber_account") REFERENCES "subscriber"("account"),
  FOREIGN KEY ("service_id") REFERENCES "service"("id")
);

CREATE SEQUENCE serial_payment_type START 1;

CREATE TABLE "payment_type" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_payment_type'),
  "name" VARCHAR(64) NOT NULL
);

CREATE SEQUENCE serial_payment START 1;

CREATE TABLE "payment" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_payment'),
  "subscriber_account" INTEGER NOT NULL,
  "service_id" INTEGER NOT NULL,
  "rendered_service_id" INTEGER,
  "payment_type_id"  INTEGER,
  "date" DATE,
  "price" NUMERIC DEFAULT 0,
  "bank_file_name" VARCHAR(64),
  FOREIGN KEY ("subscriber_account") REFERENCES "subscriber"("account"),
  FOREIGN KEY ("service_id") REFERENCES "service"("id"),
  FOREIGN KEY ("rendered_service_id") REFERENCES "rendered_service"("id"),
  FOREIGN KEY ("payment_type_id") REFERENCES "payment_type"("id")
);

CREATE SEQUENCE serial_material_consumption START 1;

CREATE TABLE "material_consumption" (
  "id" INTEGER PRIMARY KEY DEFAULT nextval('serial_material_consumption'),
  "material_id" INTEGER NOT NULL,
  "rendered_service_id" INTEGER NOT NULL,
  "amount" DOUBLE PRECISION DEFAULT 0 NOT NULL,
  FOREIGN KEY ("material_id") REFERENCES "material"("id"),
  FOREIGN KEY ("rendered_service_id") REFERENCES "rendered_service"("id")
);

CREATE TABLE "period" (
  "date" DATE NOT NULL,
  PRIMARY KEY("date")
);
