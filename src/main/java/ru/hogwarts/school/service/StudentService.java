package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return this.studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return this.studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);  //404 если не найден
    }

    public Student editStudent(Long id, Student student) {
        Student dbStudent =
                this.studentRepository.findById(id).orElseThrow(StudentNotFoundException::new); //404 если не найден
        dbStudent.setName(student.getName());
        dbStudent.setAge(student.getAge());
        return this.studentRepository.save(dbStudent);
    }

    public void removeStudent(Long id) {
        this.studentRepository.deleteById(id);  //500 если нет такого id и ошибки в консоль спринга; 204 если удалил
    }

    public Collection<Student> getAllStudents() {
        return this.studentRepository.findAll();
    }

    public Collection<Student> getByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        return this.studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getFacultyByStudentId(Long id) {
        return this.studentRepository.findById(id).get().getFaculty();
    }
}
