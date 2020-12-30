DROP TYPE IF EXISTS gender CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS timeframes CASCADE;
DROP TABLE IF EXISTS lessons CASCADE;
DROP TABLE IF EXISTS courses_rooms;
DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS teachers_courses;
DROP TABLE IF EXISTS lessons_groups;

CREATE TYPE gender AS ENUM(
	'MALE',
	'FEMALE'
);

CREATE TABLE courses(
	id SERIAL PRIMARY KEY,
	name VARCHAR ( 100 ) UNIQUE NOT NULL,
	description VARCHAR ( 250 ) DEFAULT NULL
);

CREATE TABLE rooms(
	id SERIAL PRIMARY KEY,
	name VARCHAR ( 100 ) UNIQUE NOT NULL,
	capacity INT NOT NULL
);

CREATE TABLE courses_rooms(
	course_id INT NOT NULL,
	room_id INT NOT NULL,
	FOREIGN KEY (course_id)
		REFERENCES courses(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (room_id)
		REFERENCES rooms(id) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE (course_id, room_id)
);

CREATE TABLE groups(
	id SERIAL PRIMARY KEY,
	name VARCHAR ( 100 ) UNIQUE NOT NULL
);

CREATE TABLE students(
	id SERIAL PRIMARY KEY,
	group_id INT DEFAULT NULL,
	name VARCHAR ( 100 ) NOT NULL,
	surname VARCHAR ( 100 ) NOT NULL,
	phone VARCHAR ( 100 ) DEFAULT NULL,
	email VARCHAR ( 100 ) DEFAULT NULL,
	address VARCHAR ( 100 ) DEFAULT NULL,
	birth_date DATE DEFAULT NULL,
	gender VARCHAR ( 20 ) NOT NULL,
	CONSTRAINT fk_groups
		FOREIGN KEY(group_id)
			REFERENCES groups(id)
);

CREATE TABLE students_courses(
	student_id INT NOT NULL,
	course_id  INT NOT NULL,
	FOREIGN KEY (student_id)
		REFERENCES students(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (course_id)
		REFERENCES courses(id) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE (student_id, course_id)	
);

CREATE TABLE teachers(
	id SERIAL PRIMARY KEY,
	name VARCHAR ( 100 ) NOT NULL,
	surname VARCHAR ( 100 ) NOT NULL,
	rank VARCHAR ( 100 ) DEFAULT NULL,
	phone VARCHAR ( 100 ) DEFAULT NULL,
	email VARCHAR ( 100 ) DEFAULT NULL,
	address VARCHAR ( 100 ) DEFAULT NULL,
	birth_date DATE DEFAULT NULL,
	gender VARCHAR ( 20 ) NOT NULL
);

CREATE TABLE teachers_courses(
	teacher_id INT NOT NULL,
	course_id  INT NOT NULL,
	FOREIGN KEY (teacher_id)
		REFERENCES teachers(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (course_id)
		REFERENCES courses(id) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE (teacher_id, course_id)	
);

CREATE TABLE timeframes(
	id SERIAL PRIMARY KEY,
	sequance INT NOT NULL,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL
);

CREATE TABLE lessons(
	id SERIAL PRIMARY KEY,
	date DATE NOT NULL,
	timeframe_id INT NOT NULL,
	course_id INT NOT NULL,
	teacher_id INT NOT NULL,
	room_id INT NOT NULL,
	CONSTRAINT fk_timeframes
		FOREIGN KEY(timeframe_id)
			REFERENCES timeframes(id),
	CONSTRAINT fk_courses
		FOREIGN KEY(course_id)
			REFERENCES courses(id),
	CONSTRAINT fk_teachers
		FOREIGN KEY(teacher_id)
			REFERENCES teachers(id),
	CONSTRAINT fk_rooms
		FOREIGN KEY(room_id)
			REFERENCES rooms(id)
);

CREATE TABLE lessons_groups(
	lesson_id INT NOT NULL,
	group_id  INT NOT NULL,
	FOREIGN KEY (lesson_id)
		REFERENCES lessons(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (group_id)
		REFERENCES groups(id) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE (lesson_id, group_id)
);