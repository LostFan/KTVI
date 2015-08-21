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

INSERT INTO "subscriber" ("id", "name") VALUES(700111, 'Иванов');
INSERT INTO "subscriber" ("id", "name") VALUES(700231, 'Петров');
INSERT INTO "subscriber" ("id", "name") VALUES(700567, 'Сидоров');
INSERT INTO "subscriber" ("id", "name") VALUES(700431, 'Романов');
INSERT INTO "subscriber" ("id", "name") VALUES(700267, 'Фролова');

INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date", "disconnection_date") VALUES(1, 700111, '2015-06-01', '2015-07-15');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date") VALUES(2, 700111, '2015-08-01');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date") VALUES(3, 700231, '2015-02-01');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date", "disconnection_date") VALUES(4, 700567, '2015-03-01', '2015-06-01');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date") VALUES(5, 700431, '2015-03-01');
INSERT INTO "subscriber_session" ("id", "subscriber_id", "connection_date") VALUES(6, 700267, '2015-03-01');

INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "disconnection_date", "tariff_id") VALUES(1, 700111, '2015-06-01', '2015-07-15', 2);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "tariff_id") VALUES(2, 700111, '2015-08-01', 2);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "tariff_id") VALUES(3, 700231, '2015-02-01', 3);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "disconnection_date", "tariff_id") VALUES(4, 700567, '2015-03-01', '2015-06-01', 1);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "tariff_id") VALUES(5, 700431, '2015-03-01', 3);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "disconnection_date", "tariff_id") VALUES(6, 700267, '2015-03-01', '2015-07-01', 1);
INSERT INTO "subscriber_tariff" ("id", "subscriber_id", "connection_date", "tariff_id") VALUES(7, 700267, '2015-07-01', 2);

INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(1, 700111, 2, '2015-06-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(2, 700111, 4, '2015-07-15');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(3, 700111, 3, '2015-08-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(4, 700231, 2, '2015-02-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(5, 700567, 2, '2015-03-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(6, 700567, 4, '2015-06-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(7, 700431, 2, '2015-03-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(8, 700267, 2, '2015-03-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(9, 700267, 5, '2015-07-01');
INSERT INTO "rendered_service" ("id", "subscriber_id", "service_id", "date") VALUES(10, 700267, 6, '2015-07-23');

INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(1, 700111, 2, '2015-06-01', 60000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(2, 700111, 4, '2015-07-15', 0);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(3, 700111, 3, '2015-08-01', 20000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(4, 700231, 2, '2015-02-01', 40000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(5, 700567, 2, '2015-03-01', 40000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(6, 700567, 4, '2015-06-01', 0);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(7, 700431, 2, '2015-03-01', 40000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(8, 700267, 2, '2015-03-01', 40000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(9, 700267, 5, '2015-07-01', 30000);
INSERT INTO "payment" ("id", "subscriber_id", "service_id", "date", "price") VALUES(10, 700267, 6, '2015-07-23', 25000);

INSERT INTO "material_consumption" ("id", "material_id", "rendered_service_id", "amount") VALUES(1, 1, 1, 2);
