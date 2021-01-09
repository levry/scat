package scat.adapter.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import scat.Entities;
import scat.TestConfig;
import scat.domain.model.City;
import scat.domain.model.School;
import scat.domain.model.SchoolType;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author levry
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class SchoolControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Test
    void get_school() throws Exception {
        SchoolType schoolType = entities.schoolType("University");
        City city = entities.city("Ekaterinburg", "Ural", "Russia");

        School school = entities.school("URFU", 20, "University", city);

        mvc.perform(get("/schools/{id}", school.getId()))
                .andDo(print())
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
    void should_be_404_if_not_found_school() throws Exception {
        mvc.perform(get("/schools/{id}", 81)).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void post_school() throws Exception {
        City city = entities.city("Ekaterinburg", "Ural", "Russia");
        SchoolType schoolType = entities.schoolType("University");

        String json = format(
                "{ \"name\": \"urfu\", \"number\": 5, \"city\": %1$s, \"type\": %2$s }",
                city.getId(), schoolType.getId()
        );
        RequestBuilder dataPost = post("/schools")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);


        mvc.perform(dataPost)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("urfu"))
                .andExpect(jsonPath("$.number").value(5))
                .andExpect(jsonPath("$.type.id").value(schoolType.getId()))
                .andExpect(jsonPath("$.type.name").value("University"))
                .andExpect(jsonPath("$.city.id").value(city.getId()))
                .andExpect(jsonPath("$.city.name").value("Ekaterinburg"));
    }


    @Test
    void update_school() throws Exception {
        SchoolType colledge = entities.schoolType("Colledge");
        SchoolType university = entities.schoolType("University");
        City ekaterinburg = entities.city("Ekaterinburg", "Ural", "Russia");
        City moscow = entities.city("Moscow", null, "Russia");

        School school = entities.school("UPI", "Colledge", moscow);

        String json = format(
                "{ \"name\": \"urfu\", \"number\": 5, \"city\": %1$s, \"type\": %2$s }",
                ekaterinburg.getId(), university.getId()
        );
        RequestBuilder dataPost = put("/schools/{id}", school.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(school.getId()))
                .andExpect(jsonPath("$.name").value("urfu"))
                .andExpect(jsonPath("$.number").value(5))
                .andExpect(jsonPath("$.type.id").value(university.getId()))
                .andExpect(jsonPath("$.type.name").value("University"))
                .andExpect(jsonPath("$.city.id").value(ekaterinburg.getId()))
                .andExpect(jsonPath("$.city.name").value("Ekaterinburg"));
    }

    @Test
    void delete_school() throws Exception {
        City city = entities.city("Ekaterinburg", "Ural", "Russia");

        School school = entities.school("URFU", 20, "University", city);

        mvc.perform(delete("/schools/{id}", school.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}