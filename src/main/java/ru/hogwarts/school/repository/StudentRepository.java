package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    @Query(value = "SELECT count(*) FROM student", nativeQuery = true)
    Long countAllStudents();

    @Query(value = "SELECT avg(age) FROM student", nativeQuery = true)
    double averageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC limit 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
//    List<Student> findLastFiveStudents(int number);
    //Найти как в @Query передать переменную number в limit, чтобы задавать количество студентов в эндпоинте
}
