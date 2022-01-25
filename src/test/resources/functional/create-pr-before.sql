delete from application;
delete from subject_experiments;
delete from employee_experiments;
delete from subject;
delete from experiment;
delete from employees_roles;
delete from employee_events;
delete from event;
delete from incident;
delete from employee;

insert into employee(id, active, first_name, last_name, email, password, department, pr_manager_id) values
(1, true, 'Эдгар', 'Свонс', 'edgar@vought.com', 'test', 'HEAD', null),
(2, true, 'Василий', 'Свонс', 'vas@vought.com', 'test', 'PR', null),
(3, true, 'Петр', 'Свонс', 'petya@vought.com', 'test', 'HERO', 2),
(4, true, 'Паша', 'Свонс', 'pasha@vought.com', 'test', 'HERO', null);
--
insert into employees_roles(employee_id, role_id) values
(1, 1),
(2, 4),
(4, 3),
(3, 3);

insert into application(id, application_status, application_type, description, meeting_time, name, reject_reason, update_date, creator_id, aim_id) values
(1, 'APPROVED', 'PR_STRATEGY',  'Описание 1', '2022-01-27 13:54:00', 'Название 1', null, '2022-01-22', 2, 1);

alter sequence hibernate_sequence restart with 100;