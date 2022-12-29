package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long counter = 0L;

    public Faculty addFaculty(Faculty faculty) {
        long newId = this.counter++;
        faculty.setId(newId);
        faculties.put(newId, faculty);
        return faculty;
    }

    public Faculty getFaculty(Long id) {
        if (this.faculties.containsKey(id)) {
            return this.faculties.get(id);
        } else {
            throw new FacultyNotFoundException();
        }
    }

    public Faculty editFaculty(Long id, Faculty faculty) {
        if (this.faculties.containsKey(id)) {
            Faculty oldFaculty = this.faculties.get(id);
            oldFaculty.setName(faculty.getName());
            oldFaculty.setColor(faculty.getColor());
            return oldFaculty;
        } else {
            throw new FacultyNotFoundException();
        }
    }

    public void removeFaculty(Long id) {
        if (this.faculties.containsKey(id)) {
            this.faculties.remove(id);
        } else {
            throw new FacultyNotFoundException();
        }
    }

    public Collection<Faculty> getAllFaculties() {
        return this.faculties.values();
    }

    public Collection<Faculty> getByColor(String color) {
        return this.faculties.values().stream()
                .filter(s -> Objects.equals(s.getColor(), color))
                .collect(Collectors.toList());
    }
}
