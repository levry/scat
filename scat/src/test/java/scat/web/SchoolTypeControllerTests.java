package scat.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import scat.Entities;
import scat.TestConfig;
import scat.data.SchoolType;

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
@ExtendWith(SpringExtension.class)
class SchoolTypeControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Test
    void get_school_type() throws Exception {
        SchoolType schoolType = entities.schoolType("Academy");

        mvc.perform(get("/school_types/{id}", schoolType.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(schoolType.getId()))
                .andExpect(jsonPath("$.name").value("Academy"));
    }

    @Test
    void should_be_404_if_not_found_type() throws Exception {
        mvc.perform(get("/school_types/{id}", 6))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void post_school_type() throws Exception {
        String json = "{ \"name\": \"University\" }";

        RequestBuilder dataPost = post("/school_types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("University"));
    }

    @Test
    void update_school_type() throws Exception {
        SchoolType schoolType = entities.schoolType("Colledge");

        String json = "{ \"name\": \"University\" }";
        RequestBuilder dataPost = put("/school_types/{id}", schoolType.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(schoolType.getId()))
                .andExpect(jsonPath("$.name").value("University"));
    }

    @Test
    void delete_school_type() throws Exception {
        SchoolType schoolType = entities.schoolType("Probe");

        mvc.perform(delete("/school_types/{id}", schoolType.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}