INSERT INTO courses (name) VALUES ('Law');
INSERT INTO courses (name) VALUES ('Biology');
INSERT INTO courses (name) VALUES ('Music');
INSERT INTO courses (name) VALUES ('Art');
INSERT INTO teachers (name, surname, birth_date, gender) VALUES ('Victor', 'Doncov', '1991-01-01', 'MALE');
INSERT INTO teachers (name, surname, birth_date, gender) VALUES ('Aleksandra', 'Ivanova', '1992-02-02', 'FEMALE');
INSERT INTO teachers (name, surname, birth_date, gender) VALUES ('Anatoly', 'Sviridov', '1993-03-03', 'MALE');
INSERT INTO teachers_courses (teacher_id, course_id) VALUES (1, 1);
INSERT INTO teachers_courses (teacher_id, course_id) VALUES (1, 2);
INSERT INTO teachers_courses (teacher_id, course_id) VALUES (1, 3);