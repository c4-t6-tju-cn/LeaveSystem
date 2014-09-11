CREATE TABLE user (
	user_id BIGINT NOT NULL, 
	user_name VARCHAR(20) NOT NULL, 
	pwd VARCHAR(20) NOT NULL,
	staff_position VARCHAR(10), 
	total_annual_leave INT,
	FOREIGN KEY (department_id) REFERENCES department(department_id),
	PRIMARY KEY (user_id),
);

CREATE TABLE department(
	department_id INT NOT NULL,
	department_abbr VARCHAR(5),
	department_name VARCHAR(30),
	PRIMARY KEY (department_id)
)

CREATE TABLE application(
	application_id INT AUTO_INCREMENT NOT NULL, 
	applicant_id BIGINT NOT NULL, 
	leave_date VARCHAR(10) NOT NULL,
	leave_length INT NOT NULL,
	leave_reason TEXT,
	leave_type VARCHAR NOT NULL,
	apply_date VARCHAR(10) NOT NULL,
	approve_status VARCHAR(10) NOT NULL,
	FOREIGN KEY(applicant_id) REFERENCES user(user_id), 
	PRIMARY KEY(id)
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

insert user values(3011218160, 'zss', '6606709',  'manager', 360 )
insert user values(3011218162, 'ztbxxt', '921121',  'manager', 360 )
insert user values(9000218001, 'testuser1', '123',  'employee', 5)
insert user values(3011218145, 'wangjian', 'wangjian', 'manager', 10);

--持续更新