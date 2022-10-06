--COUNTRY DATA FOR SEEDING--
-- insert into country (multiplier, name, shorthand) values (5, 'Germany', 'DE');
-- insert into country (multiplier, name, shorthand) values (10, 'Spain', 'ESP');
-- insert into country (multiplier, name, shorthand) values (8, 'England', 'ENG');
-- insert into country (multiplier, name, shorthand) values (7, 'Poland', 'PL');
-- insert into country (multiplier, name, shorthand) values (0, 'Sweden', 'SE');
-- insert into country (multiplier, name, shorthand) values (0, 'Norway', 'NO');
-- insert into country (multiplier, name, shorthand) values (0, 'Denmark', 'DK');
--ACCOUNT AND SHIPMENT DATA FOR SEEDING--
-- insert into account (contact_number, created_at, date_of_birth, email, postal_code, country_id)
-- values ('+5479028297',now(), TO_TIMESTAMP('2016-02-10 08:10:00', 'YYYY-MM-DD HH24:MI:SS'), 'Maria@live.de','73273', 1);
-- insert into account (email) values ('George@live.de');
-- insert into shipment (box_color, receiver_name, weight_option, country_id, account_id, cost) values ('#FFFFFF', 'Neon Leon', 8, 1, 1, 240);
-- insert into shipment (box_color, receiver_name, weight_option, country_id, account_id, cost) values ('#FFFFFF', 'Dennis M', 5, 2, 2, 250);
--STATUS DATA FOR SEEDING--
insert into status(status) values ('CREATED');
insert into status(status) values ('RECEIVED');
insert into status(status) values ('INTRANSIT');
insert into status(status) values ('COMPLETED');
insert into status(status) values ('CANCELLED');
--SHIPMENT_STATUS DATA FOR SEEDING--
-- insert into shipment_status(status_id, changed_time, shipment_id) values (1, now(), 1);
-- insert into shipment_status(status_id, changed_time, shipment_id) values (2, now(), 1);
-- insert into shipment_status(status_id, changed_time, shipment_id) values (1, now(), 1);
-- insert into shipment_status(changed_time, shipment_id, status_id) values (now(), 2, 1);