DROP TYPE IF EXISTS gender CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS timeframes CASCADE;
DROP TABLE IF EXISTS lessons;
DROP TABLE IF EXISTS courses_rooms;
DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS teachers_courses;

CREATE TYPE gender AS ENUM(
	'MALE',
	'FEMALE'
);

CREATE TABLE courses(
	course_id SERIAL PRIMARY KEY,
	course_name VARCHAR ( 100 ) UNIQUE NOT NULL,
	course_description VARCHAR ( 250 ) DEFAULT NULL
);

CREATE TABLE rooms(
	room_id SERIAL PRIMARY KEY,
	room_name VARCHAR ( 100 ) UNIQUE NOT NULL,
	room_capacity INT NOT NULL
);

CREATE TABLE courses_rooms(
	course_id INT NOT NULL,
	room_id INT NOT NULL,
	FOREIGN KEY (course_id)
		REFERENCES courses(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (room_id)
		REFERENCES rooms(room_id) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE (course_id, room_id)
);

CREATE TABLE groups(
	group_id SERIAL PRIMARY KEY,
	group_name VARCHAR ( 100 ) UNIQUE NOT NULL
);

CREATE TABLE students(
	student_id SERIAL PRIMARY KEY,
	group_id INT DEFAULT NULL,
	student_name VARCHAR ( 100 ) NOT NULL,
	student_surname VARCHAR ( 100 ) NOT NULL,
	student_phone VARCHAR ( 100 ) DEFAULT NULL,
	student_email VARCHAR ( 100 ) DEFAULT NULL,
	student_address VARCHAR ( 100 ) DEFAULT NULL,
	student_birthdate DATE DEFAULT NULL,
	student_gender gender DEFAULT NULL,
	CONSTRAINT fk_groups
		FOREIGN KEY(group_id)
			REFERENCES groups(group_id)
);

CREATE TABLE students_courses(
	student_id INT NOT NULL,
	course_id  INT NOT NULL,
	FOREIGN KEY (student_id)
		REFERENCES students(student_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (course_id)
		REFERENCES courses(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE (student_id, course_id)	
);

CREATE TABLE teachers(
	teacher_id SERIAL PRIMARY KEY,
	teacher_name VARCHAR ( 100 ) NOT NULL,
	teacher_surname VARCHAR ( 100 ) NOT NULL,
	teacher_rank VARCHAR ( 100 ) DEFAULT NULL,
	teacher_phone VARCHAR ( 100 ) DEFAULT NULL,
	teacher_email VARCHAR ( 100 ) DEFAULT NULL,
	teacher_address VARCHAR ( 100 ) DEFAULT NULL,
	teacher_birthdate DATE DEFAULT NULL,
	teacher_gender gender DEFAULT NULL
);

CREATE TABLE teachers_courses(
	teacher_id INT NOT NULL,
	course_id  INT NOT NULL,
	FOREIGN KEY (teacher_id)
		REFERENCES teachers(teacher_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (course_id)
		REFERENCES courses(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE (teacher_id, course_id)	
);

CREATE TABLE timeframes(
	timeframe_id SERIAL PRIMARY KEY,
	timeframe_sequance INT NOT NULL,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL
);

CREATE TABLE lessons(
	lesson_date DATE NOT NULL,
	timeframe_id INT NOT NULL,
	group_id INT NOT NULL,
	course_id INT NOT NULL,
	teacher_id INT NOT NULL,
	room_id INT NOT NULL,
	CONSTRAINT fk_timeframes
		FOREIGN KEY(timeframe_id)
			REFERENCES timeframes(timeframe_id),
	CONSTRAINT fk_groups
		FOREIGN KEY(group_id)
			REFERENCES groups(group_id),
	CONSTRAINT fk_courses
		FOREIGN KEY(course_id)
			REFERENCES courses(course_id),
	CONSTRAINT fk_teachers
		FOREIGN KEY(teacher_id)
			REFERENCES teachers(teacher_id),
	CONSTRAINT fk_rooms
		FOREIGN KEY(room_id)
			REFERENCES rooms(room_id)
);