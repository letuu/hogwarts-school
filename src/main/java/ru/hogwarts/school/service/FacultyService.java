package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return this.facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        return this.facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }

    @Transactional
    public Faculty editFaculty(Long id, Faculty faculty) {
        return this.facultyRepository
                .findById(id)
                .map(fac -> {
                    fac.setName(faculty.getName());
                    fac.setColor(faculty.getColor());
                    return fac;
                })
                .orElseThrow(FacultyNotFoundException::new);
    }

    public Faculty removeFaculty(Long id) {
        Faculty dbFaculty =
                this.facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        this.facultyRepository.delete(dbFaculty);   //404 если не нашел; 204 если удалил, без Response body
        return dbFaculty;
    }

    public Collection<Faculty> getAllFaculties() {
        return this.facultyRepository.findAll();
    }

    public Collection<Faculty> getByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getByName(String name) {
        return facultyRepository.findByName(name);
    }

    public Faculty findFacultyByNameOrColor(String nameOrColor) {
        return this.facultyRepository
                .findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor)
                .orElseThrow(FacultyNotFoundException::new);
    }

    public Collection<Student> getStudentByFacultyId(long id) {
        return this.facultyRepository.findById(id).get().getStudents();
    }
}
