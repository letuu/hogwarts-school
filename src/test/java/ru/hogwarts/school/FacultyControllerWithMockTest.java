package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Разбор домашки в подсказке
@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerWithMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetFaculty() throws Exception {
        Long id = 1L;
        String name = "Hufflepuff";
        String color = "Green";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void testFindFacultiesByColor() throws Exception {
        long id1 = 1L;
        String name1 = "Hufflepuff";

        long id2 = 2L;
        String name2 = "Griffindor";

        String color = "Yellow";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color);

//        when(facultyRepository.findByColor(color)).thenReturn(Set.of(faculty1, faculty2)); //у автора
        when(facultyRepository.findByColor(color)).thenReturn(List.of(faculty1, faculty2)); //не работает

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/")
                        .queryParam("color", color)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty1, faculty2))));
    }

    @Test
    public void testFindFacultiesByColorOrNameIgnoreCase() throws Exception {
        long id1 = 1L;
        String name1 = "Hufflepuff";
        String color1 = "Yellow";
        String color1IgnoreCase = "YeLLoW";

        long id2 = 2L;
        String name2 = "Griffindor";
        String name2IgnoreCase = "GrIFFindoR";
        String color2 = "Red";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

//        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(color1IgnoreCase, name2IgnoreCase)).thenReturn(Set.of(faculty1, faculty2)); //у автора
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(color1IgnoreCase, name1)).thenReturn(Optional.of(faculty1));
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(color2, name2IgnoreCase)).thenReturn(Optional.of(faculty2));

        //Это не будет работать у меня, так как у автора в эндпоинте два параметра, а у меня один
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/")
                        .queryParam("color", color1IgnoreCase)
                        .queryParam("name", name2IgnoreCase)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty1, faculty2))));
    }

    @Test
    public void testAddFaculty() throws Exception {
        Long id = 1L;
        String name = "Hufflepuff";
        String color = "Green";

        JSONObject facultyObj = new JSONObject();
        facultyObj.put("name", name);
        facultyObj.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObj.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void testUpdateFaculty() throws Exception {
        Long id = 1L;
        String oldName = "Hufflepuff";
        String oldColor = "Green";

        String newName = "Ravenclaw";
        String newColor = "Blue";

        JSONObject facultyObj = new JSONObject();
        facultyObj.put("id", id);
        facultyObj.put("name", newName);
        facultyObj.put("color", newColor);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(oldName);
        faculty.setColor(oldColor);

        Faculty updatedFaculty = new Faculty();
        faculty.setId(id);
        faculty.setName(newName);
        faculty.setColor(newColor);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObj.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.color").value(newColor));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        Long id = 1L;
        String name = "Hufflepuff";
        String color = "Green";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.getReferenceById(id)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        verify(facultyRepository, atLeastOnce()).deleteById(id);
    }
}
