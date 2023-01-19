package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method for add student: {}", student);
        return this.studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("Was invoked method for getting student by id = " + id);
        return this.studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);  //404 если не найден
    }

    public Student editStudent(Long id, Student student) {
        Student dbStudent =
                this.studentRepository.findById(id).orElseThrow(StudentNotFoundException::new); //404 если не найден
        logger.debug("Student {} with id {} will be edited", dbStudent, id);
        dbStudent.setName(student.getName());
        dbStudent.setAge(student.getAge());
        logger.info("Was invoked method for edit student. Updated student: {}", dbStudent);
        return this.studentRepository.save(dbStudent);
    }

    public void removeStudent(Long id) {
        logger.info("Was invoked method to remove a student with id = " + id);
        logger.warn("If the student with id={} does not exist, then there will be an error with code 500", id);
        this.studentRepository.deleteById(id);  //500 если нет такого id и ошибки в консоль спринга; 204 если удалил
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return this.studentRepository.findAll();
    }

    public Collection<Student> getByAge(int age) {
        logger.info("Was invoked method to get students with age = " + age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method to find students aged {} to {}", minAge, maxAge);
        return this.studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getFacultyByStudentId(Long id) {
        logger.info("Was invoked method to get the faculty of the student with id = " + id);
        return this.studentRepository.findById(id).get().getFaculty();
    }

    public Long getNumberAllStudents() {
        logger.info("Was invoked method to get number of students");
        return this.studentRepository.countAllStudents();
    }

    public double getAverageAgeStudents() {
        logger.info("Was invoked method to get the average age of students");
        return this.studentRepository.averageAge();
    }

    public double getAverageAgeStudentsStream() {
        logger.info("Was invoked method to get the average age of students via stream");
        return this.studentRepository.findAll().stream()
//                .map(s -> s.getAge())
                .map(Student::getAge)
                .mapToDouble(a -> a)
                .average()
                .orElse(0);
    }

    public Collection<Student> getLastStudents(int number) {
        logger.info("Was invoked method to get the last {} students", number);
        return studentRepository.findLastStudents(number);
    }

    public Collection<String> getNamesWithLetterA(String firstLetter) {
        logger.info("Was invoked method to get of names with the letter '{}'", firstLetter);
        return this.studentRepository.findAll().stream()
                .map(s -> s.getName().toUpperCase())
                .filter(n -> n.startsWith(firstLetter.toUpperCase()))
                .sorted()
                .toList();
    }

    //Вычисление формулы со способом уменьшения времени ответа эндпоинта в виде добавления parallel()
    public int getSumOfNumbers() {
        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);    //или .reduce(0, Integer::sum)
        return sum;
    }

    public void printNamesStudents() {
        List<Student> students = this.studentRepository.findAll();
        List<String> namesStudents = students.stream()
                .map(Student::getName)
                .toList();
        System.out.println(namesStudents.get(0));
        System.out.println(namesStudents.get(1));
        new Thread(() -> {
            System.out.println(namesStudents.get(2));
            System.out.println(namesStudents.get(3));
        }).start();
        new Thread(() -> {
            System.out.println(namesStudents.get(4));
            System.out.println(namesStudents.get(5));
        }).start();
    }

    public void printNamesStudentsSynchronized() {
        List<Student> students = this.studentRepository.findAll();
        List<String> namesStudents = students.stream()
                .map(Student::getName)
                .toList();
        printNames(namesStudents, 0, 1);
        new Thread(() -> printNames(namesStudents, 2, 3)).start();
        new Thread(() -> printNames(namesStudents, 4, 5)).start();
    }

    private synchronized void printNames(List<String> names, int index1, int index2) {
        System.out.println(names.get(index1));
        System.out.println(names.get(index2));
    }
}
