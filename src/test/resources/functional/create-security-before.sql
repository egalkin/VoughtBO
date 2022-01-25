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

insert into employee(id, active, first_name, last_name, email, password, department) values
(1, true, 'Эдгар', 'Свонс', 'edgar@vought.com', 'test', 'HEAD'),
(2, true, 'Василий', 'Свонс', 'vas@vought.com', 'test', 'SECURITY');
--
insert into employees_roles(employee_id, role_id) values
(1, 1),
(2, 5);

insert into incident(id, active, address, armament_level, enemies_number, incident_type, info, creator_id) values
(1, false , 'Адрес', 5, 5, 'ROBBERY', 'Инф', 2),
(2, true , 'Адрес', 5, 5, 'ROBBERY', 'Инф', 2);

alter sequence hibernate_sequence restart with 100;