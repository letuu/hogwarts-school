-- liquibase formatted sql

-- changeset albert-t:1
CREATE INDEX student_name_index ON student (name);

-- changeset albert-t:2
CREATE INDEX faculty_name_color_index ON faculty (name, color);
