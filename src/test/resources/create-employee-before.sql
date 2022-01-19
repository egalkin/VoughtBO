delete from application;
delete from employees_roles;
delete from employee;
-- delete from application;
-- delete from employee_events;
-- delete from employee_experiments;
--
-- delete from event;
-- delete from incident;
-- -- delete from subject_experiment;
--
--
--
insert into employee(id, first_name, last_name, email, password, department) values
(1, 'Эдгар', 'Свонс', 'edgar@vought.com', 'test', 'HEAD'),
(2, 'Антонина', 'Свонс', 'ant@vought.com', 'test', 'LABORATORY'),
(3, 'Василий', 'Свонс', 'vas@vought.com', 'test', 'PR'),
(4, 'Петр', 'Свонс', 'petya@vought.com', 'test', 'HERO');
--
insert into employees_roles(employee_id, role_id) values
(1, 1),
(2, 6),
(4, 3),
(3, 4);

alter sequence hibernate_sequence restart with 100;





