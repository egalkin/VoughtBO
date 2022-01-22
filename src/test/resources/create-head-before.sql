delete from application;
delete from employees_roles;
delete from employee_events;
delete from event;
delete from incident;
delete from employee;


-- delete from application;
--
-- delete from employee_experiments;
--
--
-- delete from incident;
-- -- delete from subject_experiment;
--
--
--
insert into employee(id, active, first_name, last_name, email, password, department) values
(1, true, 'Эдгар', 'Свонс', 'edgar@vought.com', 'test', 'HEAD'),
(2, true, 'Антонина', 'Свонс', 'ant@vought.com', 'test', 'LABORATORY'),
(3, true, 'Василий', 'Свонс', 'vas@vought.com', 'test', 'PR'),
(4, true, 'Петр', 'Свонс', 'petya@vought.com', 'test', 'HERO'),
(5, true, 'Паша', 'Свонс', 'pasha@vought.com', 'test', 'SECURITY');
--
insert into employees_roles(employee_id, role_id) values
(1, 1),
(2, 6),
(4, 3),
(3, 4),
(5, 5);                                                 ;

insert into application(id, application_status, application_type, description, meeting_time, name, reject_reason, update_date, creator_id, aim_id) values
(1, 'APPROVED', 'MEETING',  'Описание 1', '2022-01-27 13:54:00', 'Название 1', null, '2022-01-22', 3, 1),
(2, 'REJECTED', 'PR_STRATEGY', 'Описание 2', '2022-01-27 13:54:00', 'Название 2', 'Причина отказа', '2022-01-22', 3, 1),
(3, 'PENDING', 'RESEARCH', 'Описание 3', '2022-01-27 13:54:00', 'Название 3', null, '2022-01-22', 2, 1);

insert into event(id, address, description, event_time, name, priority, creator_id, aim_id) values
(1, 'Адрес встречи', 'описание встречи', '2022-01-24 13:33:00', 'Название 1', 3, 4, 1);

insert into employee_events(employee_id, event_id) values
(1, 1),
(4, 1);

insert into incident(id, active, address, armament_level, enemies_number, incident_type, info, creator_id) values
(1, false , 'Адрес', 5, 5, 'ROBBERY', 'Инф', 5),
(2, true , 'Адрес', 5, 5, 'ROBBERY', 'Инф', 5);

alter sequence hibernate_sequence restart with 100;




