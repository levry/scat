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
import scat.data.Country;

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
class CountryControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Test
    void get_country_by_id() throws Exception {
        Country country = entities.country("Russia");

        mvc.perform(get("/countries/{id}", country.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(country.getId()))
                .andExpect(jsonPath("$.name").value("Russia"));
    }

    @Test
    void should_be_404_if_not_found_by_id() throws Exception {
        mvc.perform(get("/countries/4"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void post_country() throws Exception {
        String json = "{ \"name\": \"Russia\" }";

        RequestBuilder dataPost = post("/countries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Russia"));
    }

    @Test
    void update_country() throws Exception {
        Country country = entities.country("France");

        String json = "{ \"name\": \"France (update)\" }";

        RequestBuilder dataPost = put("/countries/{id}", country.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(country.getId()))
                .andExpect(jsonPath("$.name").value("France (update)"));
    }

    @Test
    void delete_country() throws Exception {
        Country country = entities.country("Dropland");

        mvc.perform(delete("/countries/{id}", country.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}