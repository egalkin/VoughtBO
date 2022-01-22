delete from application;
delete from subject_experiments;
delete from employee_experiments;
delete from subject;
delete from experiment;
delete from employees_roles;
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
(3, true, 'Василий', 'Свонс', 'vas@vought.com', 'test', 'LABORATORY');
--
insert into employees_roles(employee_id, role_id) values
(1, 1),
(2, 6),
(3, 6);

insert into application(id, application_status, application_type, description, meeting_time, name, reject_reason, update_date, creator_id, aim_id) values
(1, 'APPROVED', 'RESEARCH',  'Описание 1', '2022-01-27 13:54:00', 'Название 1', null, '2022-01-22', 3, 1),
(2, 'REJECTED', 'EQUIPMENT', 'Описание 2', '2022-01-27 13:54:00', 'Название 2', 'Причина отказа', '2022-01-22', 3, 1);

insert into experiment(id, description, goal, name, creator_id) values
(1, 'Описание эксперимента 1', 'Цель эксперимента 1', 'Эксперимент 1', 2);

insert into subject(id, nickname, mentor_id) values
(1, 'Подопечный 1', 2),
(2, 'Подопечный 2', 2),
(3, 'Подопечный 3', 2);

insert into subject_experiments(subject_id, experiment_id) values
(1, 1),
(2, 1);

-- delete from subject where id=100;

alter sequence hibernate_sequence restart with 100;
