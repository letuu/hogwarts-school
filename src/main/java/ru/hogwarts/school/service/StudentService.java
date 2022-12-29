package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long counter = 0L;

    public Student addStudent(Student student) {
        long newId = this.counter++;
        student.setId(newId);
        students.put(newId, student);
        return student;
    }

    public Student getStudent(Long id) {
        if (this.students.containsKey(id)) {
            return this.students.get(id);
        } else {
            throw new StudentNotFoundException();
        }
    }

    public Student editStudent(Long id, Student student) {
        if (this.students.containsKey(id)) {
            Student oldStudent = this.students.get(id);
            oldStudent.setName(student.getName());
            oldStudent.setAge(student.getAge());
            return oldStudent;
        } else {
            throw new StudentNotFoundException();
        }
    }

    public void removeStudent(Long id) {
        if (this.students.containsKey(id)) {
            this.students.remove(id);
        } else {
            throw new StudentNotFoundException();
        }
    }

    public Collection<Student> getAllStudents() {
        return this.students.values();
    }

    public Collection<Student> getByAge(int age) {
        return this.students.values().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
    }
}
