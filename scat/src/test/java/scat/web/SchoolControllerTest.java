package scat.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import scat.Entities;
import scat.data.City;
import scat.data.School;
import scat.data.SchoolType;
import scat.repo.SchoolRepository;

import javax.persistence.EntityManager;

import static java.lang.String.format;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author levry
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SchoolControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SchoolRepository repository;

    @Autowired
    private EntityManager entityManager;

    private Entities entities;

    @Before
    public void setUp() {
        entities = new Entities(entityManager);
    }

    @Test
    @Transactional
    public void getSchool() throws Exception {
        SchoolType schoolType = entities.schoolType("University");
        City city = entities.city("Ekaterinburg", "Ural", "Russia");

        School school = new School();
        school.setId(2L);
        school.setName("URFU");
        school.setNumber(20);
        school.setType(schoolType);
        school.setCity(city);
        when(repository.getOne(eq(2L))).thenReturn(school);

        mvc.perform(get("/schools/{id}", school.getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(school.getId()))
                .andExpect(jsonPath("$.name").value("URFU"))
                .andExpect(jsonPath("$.number").value(20))
                .andExpect(jsonPath("$.type.id").value(schoolType.getId()))
                .andExpect(jsonPath("$.type.name").value(schoolType.getName()))
                .andExpect(jsonPath("$.city.id").value(city.getId()))
                .andExpect(jsonPath("$.city.name").value("Ekaterinburg"));
    }

    @Test
    @Transactional
    public void postSchool() throws Exception {
        City city = entities.city("Ekaterinburg", "Ural", "Russia");
        SchoolType schoolType = entities.schoolType("University");

        when(repository.save(any(School.class))).then(invocation -> {
            School school = invocation.getArgumentAt(0, School.class);
            school.setId(2L);
            return school;
        });


        String json = format(
                "{ \"name\": \"urfu\", \"number\": 5, \"city\": %1$s, \"type\": %2$s }",
                city.getId(), schoolType.getId()
        );
        RequestBuilder dataPost = post("/schools")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);


        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("urfu"))
                .andExpect(jsonPath("$.number").value(5))
                .andExpect(jsonPath("$.type.id").value(schoolType.getId()))
                .andExpect(jsonPath("$.type.name").value("University"))
                .andExpect(jsonPath("$.city.id").value(city.getId()))
                .andExpect(jsonPath("$.city.name").value("Ekaterinburg"));
    }


    @Test
    @Transactional
    public void updateSchool() throws Exception {
        SchoolType colledge = entities.schoolType("Colledge");
        City moscow = entities.city("Moscow", null, "Russia");

        School school = new School();
        school.setId(2L);
        school.setName("UPI");
        school.setCity(moscow);
        school.setType(colledge);

        when(repository.getOne(eq(2L))).thenReturn(school);
        when(repository.save(any(School.class))).thenReturn(school);


        SchoolType university = entities.schoolType("University");
        City ekaterinburg = entities.city("Ekaterinburg", "Ural", "Russia");


        String json = format(
                "{ \"name\": \"urfu\", \"number\": 5, \"city\": %1$s, \"type\": %2$s }",
                ekaterinburg.getId(), university.getId()
        );
        RequestBuilder dataPost = put("/schools/{id}", school.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);


        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("urfu"))
                .andExpect(jsonPath("$.number").value(5))
                .andExpect(jsonPath("$.type.id").value(university.getId()))
                .andExpect(jsonPath("$.type.name").value("University"))
                .andExpect(jsonPath("$.city.id").value(ekaterinburg.getId()))
                .andExpect(jsonPath("$.city.name").value("Ekaterinburg"));
    }

    @Test
    public void deleteSchool() throws Exception {
        mvc.perform(delete("/schools/{id}", 15L)).andDo(print())
                .andExpect(status().isNoContent());

        verify(repository).delete(eq(15L));
    }

}