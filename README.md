# Application

Spring Boot 2 - OAuth Authentication Example

## Usage

```sql
CREATE DATABASE "school-z" WITH OWNER = postgres ENCODING = 'UTF8' CONNECTION LIMIT = -1;
-- Sequence
create sequence s_class_id_seq INCREMENT 1 MINVALUE  1 MAXVALUE 9223372036854775807 START 88 CACHE 1;
create sequence s_student_id_seq INCREMENT 1 MINVALUE  1 MAXVALUE 9223372036854775807 START 88 CACHE 1;
create sequence s_exam_id_seq INCREMENT 1 MINVALUE  1 MAXVALUE 9223372036854775807 START 88 CACHE 1;
create sequence s_student_exam_id_seq INCREMENT 1 MINVALUE  1 MAXVALUE 9223372036854775807 START 88 CACHE 1;
create sequence s_exam_class_id_seq INCREMENT 1 MINVALUE  1 MAXVALUE 9223372036854775807 START 88 CACHE 1;

-- Class
CREATE TABLE s_class (
                       id serial PRIMARY KEY,
                       name VARCHAR ( 50 ) NOT NULL
);
-- Student
CREATE TABLE s_student (
                         id serial PRIMARY KEY,
                         name VARCHAR ( 50 ) NOT NULL,
                         surname VARCHAR ( 50 ) NOT NULL,
                         student_number VARCHAR ( 21 ) UNIQUE NOT NULL,
                         gender VARCHAR ( 10 ) NULL,
                         address VARCHAR ( 255 ) NULL,
                         created_on TIMESTAMP NOT NULL,
                         birthday TIMESTAMP NULL,
                         s_class_id int4 NULL
);
alter table s_student add constraint fk_s_student_class_id foreign key (s_class_id) references s_class (id) on delete restrict on update restrict;
-- Exam
CREATE TABLE s_exam (
                      id serial PRIMARY KEY,
                      exam_code VARCHAR ( 50 ) NOT NULL,
                      exam_date TIMESTAMP NOT NULL,
                      exam_location VARCHAR ( 50 ) NULL,
                      exam_time int4 NULL
);

-- Student_Exam
CREATE TABLE s_student_exam (
                              id serial PRIMARY KEY,
                              student_id int4 NULL,
                              exam_id int4 NOT NULL,
                              score int4 NOT NULL
);
alter table s_student_exam add constraint fk_s_sexam_student_id foreign key (student_id) references s_student (id) on delete restrict on update restrict;
alter table s_student_exam add constraint fk_s_sexam_exam_id foreign key (exam_id) references s_exam (id) on delete restrict on update restrict;

-- Exam_Class
CREATE TABLE s_exam_class (
                            exam_id int4 NOT NULL,
                            class_id int4 NULL
);
alter table s_exam_class add constraint fk_s_sexam_class_id foreign key (class_id) references s_class (id) on delete restrict on update restrict;
alter table s_exam_class add constraint fk_s_sexam_exam_id foreign key (exam_id) references s_exam (id) on delete restrict on update restrict;

-- insert
INSERT INTO s_class (id, name) VALUES (1, 'Class 1');
INSERT INTO s_class (id, name) VALUES (2, 'Class 2');
INSERT INTO s_class (id, name) VALUES (3, 'Class 3');
INSERT INTO s_student (id, name, surname, student_number, created_on, birthday, gender, address, s_class_id) VALUES (1, 'Özgür', 'Akıncı','2011010207050', now(), null, 'MR', null , 1);
INSERT INTO s_exam (id, exam_code, exam_date, exam_location, exam_time) VALUES (1, 'e-1', now(), null, 80);
INSERT INTO s_student_exam (id, student_id, exam_id, score) VALUES (1, 1, 1, 80);
INSERT INTO s_exam_class (exam_id, class_id) VALUES (1, 1);

```

## License
[MIT](https://choosealicense.com/licenses/mit/)
