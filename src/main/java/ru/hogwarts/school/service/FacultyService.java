package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for add faculty: {}", faculty);
        return this.facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method for getting faculty by id = " + id);
        return this.facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }

    @Transactional
    public Faculty editFaculty(Long id, Faculty faculty) {
        return this.facultyRepository
                .findById(id)
                .map(fac -> {
                    fac.setName(faculty.getName());
                    fac.setColor(faculty.getColor());
                    logger.info("Was invoked method for edit faculty");
                    return fac;
                })
                .orElseThrow(FacultyNotFoundException::new);
    }

    public Faculty removeFaculty(Long id) {
        Faculty dbFaculty =
                this.facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        this.facultyRepository.delete(dbFaculty);   //404 если не нашел; 204 если удалил, без Response body
        logger.info("Was invoked method to remove the faculty with id = " + id);
        return dbFaculty;
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return this.facultyRepository.findAll();
    }

    public Collection<Faculty> getByColor(String color) {
        logger.info("Was invoked method to get the faculties with the color = " + color);
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getByName(String name) {
        logger.info("Was invoked method to get the faculties with the name = " + name);
        return facultyRepository.findByName(name);
    }

    public Faculty findFacultyByNameOrColor(String nameOrColor) {
        logger.info("Was invoked method to search a faculty by name or color");
        return this.facultyRepository
                .findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor)
                .orElseThrow(FacultyNotFoundException::new);
    }

    public Collection<Student> getStudentByFacultyId(Long id) {
        logger.info("Was invoked method to get students by faculty id = " + id);
        logger.warn("If the faculty with id={} does not exist, then there will be an error with code 500", id);
        return this.facultyRepository.findById(id).get().getStudents();
    }
}
