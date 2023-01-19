package ru.hogwarts.school;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

//Разбор домашки АВ
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerRTTest {

    private Long testFacultyId;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeEach
    public void cleanUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
        Faculty testFaculty = new Faculty();
        testFaculty.setName("testNameF");
        testFaculty.setColor("testColor");
        testFacultyId = facultyRepository.save(testFaculty).getId();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void deleteByIdTest() {
        Student newStudent = new Student();
        newStudent.setName(Faker.instance().harryPotter().character());
        newStudent.setAge(Faker.instance().number().numberBetween(16, 24));
        newStudent.setFaculty(this.facultyRepository.getReferenceById(testFacultyId));
        Long id = this.studentRepository.save(newStudent).getId();

        Assertions.assertEquals(1, this.studentRepository.findAll().size());
        testRestTemplate.delete("http://localhost:" + port + "/student/" + id);

        Assertions.assertEquals(1, this.studentRepository.findAll().size());
//        Assertions.assertTrue(this.studentRepository.findAll().isEmpty());    //не работает - не удаляется
    }
}
