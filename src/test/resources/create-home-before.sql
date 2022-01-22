delete from employees_roles;
delete from employee;

insert into employee(id, active, first_name, last_name, email, password, department) values
(1, true, 'Эдгар', 'Свонс', 'edgar@vought.com', 'test', 'HEAD'),
(2, true, 'Антонина', 'Свонс', 'ant@vought.com', 'test', 'LABORATORY'),
(3, true, 'Василий', 'Свонс', 'vas@vought.com', 'test', 'PR'),
(4, true, 'Петр', 'Свонс', 'petya@vought.com', 'test', 'HERO'),
(5, true, 'Паша', 'Свонс', 'pasha@vought.com', 'test', 'SECURITY');

insert into employees_roles(employee_id, role_id) values
(1, 1),
(2, 6),
(4, 3),
(3, 4),
(5, 5);                                                 ;

