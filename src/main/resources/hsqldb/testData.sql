INSERT INTO "material" ("id", "name", "price", "unit") VALUES(1, 'Кабель сетевой', 10000, 'м');
INSERT INTO "material" ("id", "name", "price", "unit") VALUES(2, 'Тюнер', 500000, 'шт.');
INSERT INTO "material" ("id", "name", "price", "unit") VALUES(3, 'гайка', 500, 'шт.');

INSERT INTO "service" ("id", "name", "additional") VALUES(1, 'Абонентская плата', false);
INSERT INTO "service" ("id", "name", "additional") VALUES(2, 'Подключение', false);
INSERT INTO "service" ("id", "name", "additional") VALUES(3, 'Повторное подключение', false);
INSERT INTO "service" ("id", "name", "additional") VALUES(4, 'Отключение', false);
INSERT INTO "service" ("id", "name", "additional") VALUES(5, 'Смена тарифного плана', false);
INSERT INTO "service" ("id", "name", "additional") VALUES(6, 'Настройка телевизора', true);
INSERT INTO "service" ("id", "name", "additional") VALUES(7, 'Подключение доп. точки', true);

INSERT INTO "service_price" ("service_id", "date") VALUES(1, '2015-01-01');
INSERT INTO "service_price" ("service_id", "date", "price") VALUES(2, '2015-01-01', 40000);
INSERT INTO "service_price" ("service_id", "date", "price") VALUES(2, '2015-08-01', 50000);
INSERT INTO "service_price" ("service_id", "date", "price") VALUES(3, '2015-01-01', 20000);
INSERT INTO "service_price" ("service_id", "date", "price") VALUES(4, '2015-01-01', 0);
INSERT INTO "service_price" ("service_id", "date", "price") VALUES(5, '2015-01-01', 15000);
INSERT INTO "service_price" ("service_id", "date", "price") VALUES(5, '2015-06-01', 30000);
INSERT INTO "service_price" ("service_id", "date", "price") VALUES(6, '2015-01-01', 20000);
INSERT INTO "service_price" ("service_id", "date", "price") VALUES(6, '2015-03-01', 25000);
INSERT INTO "service_price" ("service_id", "date", "price") VALUES(7, '2015-01-01', 15000);

INSERT INTO "tariff" ("id", "name", "channels") VALUES(1, 'Базовый', '10');
INSERT INTO "tariff" ("id", "name", "channels") VALUES(2, 'Дополнительный', '20');
INSERT INTO "tariff" ("id", "name", "channels") VALUES(3, 'Расширенный', '40');

INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(1, '2015-01-01', 20000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(1, '2015-05-01', 25000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(2, '2015-01-01', 30000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(2, '2015-05-01', 40000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(3, '2015-01-01', 50000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(3, '2015-05-01', 75000);

INSERT INTO "subscriber" ("id", "account", "name") VALUES(1, 700111, 'Иванов');
INSERT INTO "subscriber" ("id", "account", "name") VALUES(2, 700231, 'Петров');
INSERT INTO "subscriber" ("id", "account", "name") VALUES(3, 700567, 'Сидоров');
INSERT INTO "subscriber" ("id", "account", "name") VALUES(4, 700431, 'Романов');
INSERT INTO "subscriber" ("id", "account", "name") VALUES(5, 700267, 'Фролова');

INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date", "disconnection_date") VALUES(1, 1, '2015-06-01', '2015-07-15');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date") VALUES(2, 1, '2015-08-01');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date") VALUES(3, 2, '2015-02-01');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date", "disconnection_date") VALUES(4, 3, '2015-03-01', '2015-06-01');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date") VALUES(5, 4, '2015-03-01');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date") VALUES(6, 5, '2015-03-01');

INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "disconnection_date", "tariff_id") VALUES(1, 1, '2015-06-01', '2015-07-15', 2);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "tariff_id") VALUES(2, 1, '2015-08-01', 2);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "tariff_id") VALUES(3, 2, '2015-02-01', 3);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "disconnection_date", "tariff_id") VALUES(4, 3, '2015-03-01', '2015-06-01', 1);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "tariff_id") VALUES(5, 4, '2015-03-01', 3);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "disconnection_date", "tariff_id") VALUES(6, 5, '2015-03-01', '2015-07-01', 1);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "tariff_id") VALUES(7, 5, '2015-07-01', 2);

INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(1, 1, 2, '2015-06-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(2, 1, 4, '2015-07-15');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(3, 1, 3, '2015-08-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(4, 2, 2, '2015-02-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(5, 3, 2, '2015-03-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(6, 3, 4, '2015-06-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(7, 4, 2, '2015-03-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(8, 5, 2, '2015-03-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(9, 5, 5, '2015-07-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(10, 5, 6, '2015-07-23');

INSERT INTO "payment_type" ("id", "name") VALUES(1, 'Почта');
INSERT INTO "payment_type" ("id", "name") VALUES(2, 'Инфокиоск');
INSERT INTO "payment_type" ("id", "name") VALUES(3, 'Банк');

INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(1, 1, 2, 1, '2015-06-01', 60000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(2, 1, 4, 2, '2015-07-15', 0);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(3, 1, 3, 3, '2015-08-01', 20000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(4, 2, 2, 1, '2015-02-01', 40000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(5, 3, 2, 1, '2015-03-01', 40000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(6, 3, 4, 1, '2015-06-01', 0);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(7, 4, 2, 2, '2015-03-01', 40000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(8, 5, 2, 3, '2015-03-01', 40000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(9, 5, 5, 2, '2015-07-01', 30000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "payment_type_id", "date", "price") VALUES(10, 5, 6, 1, '2015-07-23', 25000);

INSERT INTO "material_consumption" ("id", "material_id", "rendered_service_id", "amount") VALUES(1, 1, 1, 2);
