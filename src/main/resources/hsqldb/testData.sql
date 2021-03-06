INSERT INTO "street" ("id", "name") VALUES(1, 'ул. 30 лет Победы');
INSERT INTO "street" ("id", "name") VALUES(2, 'пер. Гоголя');
INSERT INTO "street" ("id", "name") VALUES(3, 'ул. Первомайская');

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

INSERT INTO "tariff" ("id", "name", "digital", "channels") VALUES(1, 'Базовый', TRUE, '10');
INSERT INTO "tariff" ("id", "name", "digital", "channels") VALUES(2, 'Дополнительный', FALSE, '20');
INSERT INTO "tariff" ("id", "name", "digital", "channels") VALUES(3, 'Расширенный', TRUE, '40');

INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(1, '2015-01-01', 20000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(1, '2015-05-01', 25000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(2, '2015-01-01', 30000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(2, '2015-05-01', 40000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(3, '2015-01-01', 50000);
INSERT INTO "tariff_price" ("tariff_id", "date", "price") VALUES(3, '2015-05-01', 75000);

INSERT INTO "subscriber" ("account", "name", "street_id") VALUES(700111, 'Иванов', 1);
INSERT INTO "subscriber" ("account", "name", "street_id") VALUES(700231, 'Петров', 1);
INSERT INTO "subscriber" ("account", "name", "street_id") VALUES(700567, 'Сидоров', 2);
INSERT INTO "subscriber" ("account", "name", "street_id") VALUES(700431, 'Романов', 2);
INSERT INTO "subscriber" ("account", "name", "street_id") VALUES(700267, 'Фролова', 3);

INSERT INTO "subscriber_session" ("id", "subscriber_account", "connection_date", "disconnection_date") VALUES(1, 1, '2015-06-01', '2015-07-15');
INSERT INTO "subscriber_session" ("id", "subscriber_account", "connection_date") VALUES(2, 1, '2015-08-01');
INSERT INTO "subscriber_session" ("id", "subscriber_account", "connection_date") VALUES(3, 2, '2015-02-01');
INSERT INTO "subscriber_session" ("id", "subscriber_account", "connection_date", "disconnection_date") VALUES(4, 3, '2015-03-01', '2015-06-01');
INSERT INTO "subscriber_session" ("id", "subscriber_account", "connection_date") VALUES(5, 4, '2015-03-01');
INSERT INTO "subscriber_session" ("id", "subscriber_account", "connection_date") VALUES(6, 5, '2015-03-01');

INSERT INTO "subscriber_tariff" ("id", "subscriber_account", "connection_date", "disconnection_date", "tariff_id") VALUES(1, 1, '2015-06-01', '2015-07-15', 2);
INSERT INTO "subscriber_tariff" ("id", "subscriber_account", "connection_date", "tariff_id") VALUES(2, 1, '2015-08-01', 2);
INSERT INTO "subscriber_tariff" ("id", "subscriber_account", "connection_date", "tariff_id") VALUES(3, 2, '2015-02-01', 3);
INSERT INTO "subscriber_tariff" ("id", "subscriber_account", "connection_date", "disconnection_date", "tariff_id") VALUES(4, 3, '2015-03-01', '2015-06-01', 1);
INSERT INTO "subscriber_tariff" ("id", "subscriber_account", "connection_date", "tariff_id") VALUES(5, 4, '2015-03-01', 3);
INSERT INTO "subscriber_tariff" ("id", "subscriber_account", "connection_date", "disconnection_date", "tariff_id") VALUES(6, 5, '2015-03-01', '2015-07-01', 1);
INSERT INTO "subscriber_tariff" ("id", "subscriber_account", "connection_date", "tariff_id") VALUES(7, 5, '2015-07-01', 2);

INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date", "price") VALUES(1, 1, 2, '2015-06-01', 10000);
INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date", "price") VALUES(2, 1, 4, '2015-07-15', 20000);
INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date", "price") VALUES(3, 1, 3, '2015-08-01', 30000);
INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date", "price") VALUES(4, 2, 2, '2015-02-01', 20000);
INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date", "price") VALUES(5, 3, 2, '2015-03-01', 20000);
INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date", "price") VALUES(6, 3, 4, '2015-06-01', 20000);
INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date", "price") VALUES(7, 4, 2, '2015-03-01', 20000);
INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date", "price") VALUES(8, 5, 2, '2015-03-01', 20000);
INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date") VALUES(9, 5, 5, '2015-07-01');
INSERT INTO "rendered_service" ("id", "subscriber_account", "service_id", "date") VALUES(10, 5, 6, '2015-07-23');

INSERT INTO "payment_type" ("id", "name") VALUES(1, 'Почта');
INSERT INTO "payment_type" ("id", "name") VALUES(2, 'Инфокиоск');
INSERT INTO "payment_type" ("id", "name") VALUES(3, 'Банк');

INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(1, 1, 2, 1, '2015-06-01', 60000);
INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(2, 1, 4, 2, '2015-07-15', 0);
INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(3, 1, 3, 3, '2015-08-01', 20000);
INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(4, 2, 2, 1, '2015-02-01', 40000);
INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(5, 3, 2, 1, '2015-03-01', 40000);
INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(6, 3, 4, 1, '2015-06-01', 0);
INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(7, 4, 2, 2, '2015-03-01', 40000);
INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(8, 5, 2, 3, '2015-03-01', 40000);
INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(9, 5, 5, 2, '2015-07-01', 30000);
INSERT INTO "payment" ("id", "subscriber_account", "service_id", "payment_type_id", "date", "price") VALUES(10, 5, 6, 1, '2015-07-23', 25000);

INSERT INTO "material_consumption" ("id", "material_id", "rendered_service_id", "amount") VALUES(1, 1, 1, 2);
