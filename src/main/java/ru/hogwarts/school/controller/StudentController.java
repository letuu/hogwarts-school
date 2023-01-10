package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @Operation(summary = "Returns list of all students", tags = "student")
    public Collection<Student> getAllStudents() {
        return this.studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a student by id", tags = "student")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Student model",
                    content = @Content(schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content())})
    public Student getStudent(@PathVariable("id") Long id) {
        return this.studentService.getStudent(id);
    }

    @GetMapping("/age/{age}")
    public Collection<Student> getStudentsByAge(@PathVariable("age") int age) {
        return studentService.getByAge(age);
    }

    @GetMapping("/age/between")
    public Collection<Student> findStudentsByAgeBetween(
            @RequestParam("minAge") int minAge, @RequestParam("maxAge") int maxAge) {
        return this.studentService.findStudentsByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/getFaculty/{id}")
    public Faculty getFacultyByStudentId(@PathVariable("id") Long studentId) {
        return this.studentService.getFacultyByStudentId(studentId);
    }

    @GetMapping("/count")
    @Operation(summary = "Returns number of all students", tags = "student")
    public Long getNumberAllStudents() {
        return this.studentService.getNumberAllStudents();
    }

    @GetMapping("/average-age")
    @Operation(summary = "Returns the average age of students", tags = "student")
    public double getAverageAgeStudents() {
        return this.studentService.getAverageAgeStudents();
    }

    @GetMapping("/last/5")
    @Operation(summary = "Returns the last 5 students", tags = "student")
    public Collection<Student> getLastFiveStudents() {
        return this.studentService.getLastFiveStudents();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return this.studentService.addStudent(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable("id") Long id, @RequestBody Student student) {
        return this.studentService.editStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        this.studentService.removeStudent(id);
        return ResponseEntity.noContent().build();
    }
}
