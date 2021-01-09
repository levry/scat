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
import scat.domain.model.Country;
import scat.domain.model.Region;

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
class RegionControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Test
    void get_region() throws Exception {
        Country country = entities.country("Russia");
        Region region = entities.region("Sverdlovskaya obl", country);

        mvc.perform(get("/regions/{id}", region.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(region.getId()))
                .andExpect(jsonPath("$.name").value("Sverdlovskaya obl"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
    }

    @Test
    void should_be_404_if_not_found_region() throws Exception {
        mvc.perform(get("/regions/{id}", 196))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void post_region() throws Exception {

        Country country = entities.country("Russia");

        String json = format("{ \"name\": \"Test\", \"country\": %1$s}", country.getId());
        RequestBuilder dataPost = post("/regions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
    }

    @Test
    void should_be_bad_request_if_not_found_country() throws Exception {
        RequestBuilder dataPost = post("/regions")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{ \"name\": \"Test\", \"country\": 66}");

        mvc.perform(dataPost)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_name_of_region() throws Exception {
        Country country = entities.country("Russia");
        Region region = entities.region("Test", country);

        String json = String.format("{ \"name\": \"Test update\", \"country\": %d }", country.getId());
        RequestBuilder request = put("/regions/{id}", region.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test update"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
    }

    @Test
    void update_country_of_region() throws Exception {
        Country russia = entities.country("Russia");
        Country france = entities.country("France");

        Region region = entities.region("Test", russia);

        String json = String.format("{ \"name\": \"Test\", \"country\": %d }", france.getId());
        RequestBuilder request = put("/regions/{id}", region.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.country.id").value(france.getId()))
                .andExpect(jsonPath("$.country.name").value("France"));
    }

    @Test
    void delete_region() throws Exception {
        Country country = entities.country("Russia");
        Region region = entities.region("Test", country);

        mvc.perform(delete("/regions/{id}", region.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}