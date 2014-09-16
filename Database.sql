CREATE TABLE user (
	user_id BIGINT NOT NULL, 
	user_name VARCHAR(20) NOT NULL, 
	pwd VARCHAR(20) NOT NULL,
	department_id  INT,
	staff_position VARCHAR(20), 
	total_annual_leave INT,
	FOREIGN KEY (department_id) REFERENCES department(department_id),
	PRIMARY KEY (user_id)
);

CREATE TABLE department(
	department_id INT NOT NULL,
	department_abbr VARCHAR(5),
	department_name VARCHAR(30),
	PRIMARY KEY (department_id)
);

CREATE TABLE application(
	application_id INT AUTO_INCREMENT NOT NULL, 
	applicant_id BIGINT NOT NULL, 
	leave_date VARCHAR(10) NOT NULL,
	leave_length INT NOT NULL,
	leave_reason TEXT,
	leave_type VARCHAR(20) NOT NULL,
	apply_date VARCHAR(10) NOT NULL,
	status VARCHAR(10) NOT NULL,
	FOREIGN KEY(applicant_id) REFERENCES user(user_id), 
	PRIMARY KEY(application_id)
);

CREATE TABLE approval(
	approval_id INT AUTO_INCREMENT NOT NULL,
	application_id INT NOT NULL,
	auditor_id BIGINT NOT NULL, 
	approve_date VARCHAR(10) NOT NULL,
	approve_opinion TEXT,
	agreed TINYINT NOT NULL,
	FOREIGN KEY(auditor_id) REFERENCES user(user_id),
	FOREIGN KEY(application_id) REFERENCES application(application_id),
	PRIMARY KEY(approval_id)
);



insert department values(1,'AD','Administrative Department');
insert department values(2,'ED','Executive Department');
insert department values(3,'PD','Peronal Department');
insert department values(4,'FD','Financial Department');
insert department values(5,'TD','Technology Department');
insert department values(6,'SD','Sales Department');
insert department values(7,'RDD','Research and Develop Department');

insert user values(3011218160, 'zss', '6606709',  1,'manager', 360 );
insert user values(3011218162, 'ztbxxt', '921121',  2,'manager', 360 );
insert user values(9000218001, 'testuser1', '123',  3,'employee', 5);
insert user values(3011218145, 'wangjian', 'wangjian', 3,'manager', 10);

insert application (application_id, applicant_id, leave_date, leave_length, leave_reason, leave_type, apply_date, status) 
	values(1,3011218145,'2014-09-16', 10, 'ganmao','sick','2014-9-1', 'wait');
insert application values(2,3011218145,'2014-9-17', 2, 'shenghaizi','maternity','2014-9-5', 'wait');
insert application values(3,3011218145,'2014-10-17', 15, 'jiehun','marital','2014-9-10', 'wait');
insert application ( applicant_id, leave_date, leave_length, leave_reason, leave_type, apply_date, status)values(3011218145,'2014-10-17', 15, 'jiehun','marital','2014-9-10', 'wait');
--持续更新