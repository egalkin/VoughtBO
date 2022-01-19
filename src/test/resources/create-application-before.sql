delete from application;
delete from employees_roles;
delete from employee;

insert into employee(id, first_name, last_name, email, password, department) values
(1, 'Эдгар', 'Свонс', 'edgar@vought.com', 'test', 'HEAD'),
(2, 'Антонина', 'Свонс', 'ant@vought.com', 'test', 'LABORATORY'),
(3, 'Василий', 'Свонс', 'vas@vought.com', 'test', 'PR');
--
insert into employees_roles(employee_id, role_id) values
(1, 1),
(2, 6),
(3, 3);

insert into application(id, application_status, application_type, description, meeting_time, name, reject_reason, update_date, creator_id, aim_id) values
(1, 'APPROVED', 'MEETING',  'Описание 1', null, 'Название 1', null, '2021-12-25', 3, 1),
(2, 'REJECTED', 'PR_STRATEGY', 'Описание 2', null, 'Название 2', 'Причина отказа', '2021-12-25', 3, 1),
(3, 'PENDING', 'RESEARCH', 'Описание 3', null, 'Название 3', null, '2021-12-25', 2, 1);

alter sequence hibernate_sequence restart with 100;