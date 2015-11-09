SET FOREIGN_KEY_CHECKS = 0;

truncate table idea.USER;
truncate table idea.IDEA;
truncate table idea.ROLE;
truncate table idea.USER_ROLE;
truncate table idea.TAG;
truncate table idea.IDEA_TAG;
truncate table idea.COMMENT;

insert into USER (USERNAME, EMAIL, PASSWORD, CREATION_TIME) values ('admin', 'admin', '21232f297a57a5a743894a0e4a801fc3', '2015-02-28 00:00:00'); -- pass :'admin'
insert into USER (USERNAME, EMAIL, PASSWORD, CREATION_TIME) values ('FirstUser', 'first@idea.com', '81dc9bdb52d04dc20036dbd8313ed055', '2015-05-03 00:00:00'); -- pass :'1234'
insert into USER (USERNAME, EMAIL, PASSWORD, CREATION_TIME) values ('SecondUser', 'second@idea.com', '81dc9bdb52d04dc20036dbd8313ed055', '2015-05-03 00:00:00'); -- pass :'1234'
insert into USER (USERNAME, EMAIL, PASSWORD, CREATION_TIME) values ('ThirdUser', 'third@idea.com', '81dc9bdb52d04dc20036dbd8313ed055', '2015-05-03 00:00:00'); -- pass :'1234'

insert into IDEA (TITLE, DESCRIPTION, CREATION_TIME, MODIFICATION_TIME, RATING, USER_ID, LATITUDE, LONGITUDE) values ('Велопарковка', 'Офис ТК4 очень ждёт её возращения', '2015-05-13', '2015-05-13', 8, 4, 53.903200, 30.335055);
insert into IDEA (TITLE, DESCRIPTION, CREATION_TIME, MODIFICATION_TIME, RATING, USER_ID, LATITUDE, LONGITUDE) values ('Площадка для аджилити', 'Я думаю,  у многих владельцев собак возникала необходимость в такой площадке. К сожалению, в нашем городе их единицы. Предлагаю оборудовать как минимум ещё одну.', '2015-01-26', '2015-01-26', 2, 3, 53.911445, 30.328036);
insert into IDEA (TITLE, DESCRIPTION, CREATION_TIME, MODIFICATION_TIME, RATING, USER_ID, LATITUDE, LONGITUDE) values ('Видеонаблюдение для зоосада', 'Как насчёт установки системы видеонаблюдения? Могилевчане могли бы наблюдать за жизнью животных из дома в режиме реального времени', '2015-03-30', '2015-03-30', 12, 2, 53.860442, 30.258000);
insert into IDEA (TITLE, DESCRIPTION, CREATION_TIME, MODIFICATION_TIME, RATING, USER_ID, LATITUDE, LONGITUDE) values ('Бесплатный Wi-Fi', 'В Могилеве нужны зоны бесплатного доступа в интернет. Особенно на площади Славы', '2015-06-02', '2015-06-02', 23, 2, 53.894325, 30.330429);
insert into IDEA (TITLE, DESCRIPTION, CREATION_TIME, MODIFICATION_TIME, RATING, USER_ID, LATITUDE, LONGITUDE) values ('Ремонт Пушкинского моста', 'Мост давно требует полной реконструкции', '2015-04-05', '2015-04-05', 31, 4, 53.892096, 30.329662);
insert into IDEA (TITLE, DESCRIPTION, CREATION_TIME, MODIFICATION_TIME, RATING, USER_ID, LATITUDE, LONGITUDE) values ('Могилевский полумарафон', 'Предлагаю организовать городской полумарафон как для любителей, так и для профессионалов. Повысим интерес к бегу у молодёжи!', '2015-04-16', '2015-04-16', 19, 2, 53.895185, 30.330892);
insert into IDEA (TITLE, DESCRIPTION, CREATION_TIME, MODIFICATION_TIME, RATING, USER_ID, LATITUDE, LONGITUDE) values ('Котокафе', 'У нас есть антикафе, но нет котокафе. А ведь это было бы отличным местом для отдыха!', '2014-12-21', '2015-12-21', 0, 3, 53.895815, 30.333220);

insert into ROLE (NAME) values ('ADMIN');
insert into ROLE (NAME) values ('USER');

insert into USER_ROLE (USER_ID, ROLE_ID) values (1, 1);
insert into USER_ROLE (USER_ID, ROLE_ID) values (2, 2);
insert into USER_ROLE (USER_ID, ROLE_ID) values (3, 2);
insert into USER_ROLE (USER_ID, ROLE_ID) values (4, 2);

insert into TAG (NAME) values ('Спорт');
insert into TAG (NAME) values ('Транспорт');
insert into TAG (NAME) values ('Культура');
insert into TAG (NAME) values ('Животные');

insert into IDEA_TAG (IDEA_ID, TAG_ID) values (1, 2);
insert into IDEA_TAG (IDEA_ID, TAG_ID) values (2, 4);
insert into IDEA_TAG (IDEA_ID, TAG_ID) values (3, 3);
insert into IDEA_TAG (IDEA_ID, TAG_ID) values (4, 3);
insert into IDEA_TAG (IDEA_ID, TAG_ID) values (5, 2);
insert into IDEA_TAG (IDEA_ID, TAG_ID) values (6, 2);
insert into IDEA_TAG (IDEA_ID, TAG_ID) values (7, 4);

insert into COMMENT (USER_ID, IDEA_ID, BODY, CREATION_TIME, MODIFICATION_TIME, RATING) values (1, 1, 'Awesome!', '2014-08-02 00:00:00', '2014-12-24 00:00:00', 5);
insert into COMMENT (USER_ID, IDEA_ID, BODY, CREATION_TIME, MODIFICATION_TIME, RATING) values (2, 2, 'Super!', '2014-06-12 00:00:00', '2014-09-20 00:00:00', 10);
insert into COMMENT (USER_ID, IDEA_ID, BODY, CREATION_TIME, MODIFICATION_TIME, RATING) values (3, 3, 'Genius!', '2015-01-10 00:00:00', '2015-02-03 00:00:00', -6);
