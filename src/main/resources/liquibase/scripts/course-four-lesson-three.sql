--liquibase formatted sql

--changeSet dpriymak:1
CREATE INDEX student_name_index ON student(name);
--changeSet dpriymak:2
CREATE INDEX faculty_name_color_index ON faculty(color, name)