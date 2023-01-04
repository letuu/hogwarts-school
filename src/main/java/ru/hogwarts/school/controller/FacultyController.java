package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public Collection<Faculty> getAllFaculties() {
        return this.facultyService.getAllFaculties();
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable("id") long id) {
        return this.facultyService.getFaculty(id);
    }

    @GetMapping("/color/{color}")
    public Collection<Faculty> getFacultiesByColor(@PathVariable("color") String color) {
        return facultyService.getByColor(color);
    }

    @GetMapping("/name/{name}")
    public Collection<Faculty> getFacultiesByName(@PathVariable("name") String name) {
        return facultyService.getByName(name);
    }

    @GetMapping("/getStudents/{id}")
    public Collection<Student> getStudentByFacultyId(@PathVariable("id") long facultyId) {
        return this.facultyService.getStudentByFacultyId(facultyId);
    }

    @GetMapping("/search/{searchNameOrColor}")
    public Faculty findFacultyByNameOrColor(@PathVariable("searchNameOrColor") String searchNameOrColor) {
        return this.facultyService.findFacultyByNameOrColor(searchNameOrColor);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return this.facultyService.addFaculty(faculty);
    }

    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable("id") long id, @RequestBody Faculty faculty) {
        return this.facultyService.editFaculty(id, faculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable("id") long id) {
        this.facultyService.removeFaculty(id);
        return ResponseEntity.noContent().build();
    }
}
