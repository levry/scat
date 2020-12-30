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
import scat.data.City;
import scat.data.Country;
import scat.data.Region;

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
@ExtendWith(SpringExtension.class)
class CityControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Entities entities;

    @BeforeEach
    void setUp() {
        entities.cleanUp();
    }

    @Test
    void get_city() throws Exception {

        Country country = entities.country("Russia");
        Region region = entities.region("Ural", country);
        City city = entities.city("Ekaterinburg", region, country);

        mvc.perform(get("/cities/{id}", city.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(city.getId()))
                .andExpect(jsonPath("$.name").value("Ekaterinburg"))
                .andExpect(jsonPath("$.region.id").value(region.getId()))
                .andExpect(jsonPath("$.region.name").value(region.getName()))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
    }

    @Test
    void should_be_404_if_not_found_city() throws Exception {
        mvc.perform(get("/cities/{id}", 3)).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void post_city() throws Exception {
        Country country = entities.country("Russia");
        Region region = entities.region("Ural", country);

        String json = format("{ \"name\": \"Test\", \"country\": %1$d, \"region\": %2$d}",
                country.getId(), region.getId());
        RequestBuilder dataPost = post("/cities")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"))
                .andExpect(jsonPath("$.region.id").value(region.getId()))
                .andExpect(jsonPath("$.region.name").value("Ural"));
    }

    @Test
    void save_city_without_region() throws Exception {
        Country country = entities.country("Russia");

        String json = format("{ \"name\": \"Test\", \"country\": %1$d }", country.getId());
        RequestBuilder dataPost = post("/cities")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
    }

    @Test
    void update_name_of_city() throws Exception {
        Country country = entities.country("Russia");
        Region region = entities.region("Ural", country);
        City city = entities.city("Test", region, country);

        String json = String.format("{ \"name\": \"Test update\", \"country\": %1$d, \"region\": %2$d }",
                country.getId(), region.getId());
        RequestBuilder request = put("/cities/{id}", city.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(city.getId()))
                .andExpect(jsonPath("$.name").value("Test update"))
                .andExpect(jsonPath("$.region.id").value(region.getId()))
                .andExpect(jsonPath("$.region.name").value("Ural"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
    }

    @Test
    void update_region_of_city() throws Exception {
        Country country = entities.country("Russia");
        Region ural = entities.region("Ural", country);
        Region syberia = entities.region("Syberia", country);

        City city = entities.city("Test", ural, country);

        String json = String.format("{ \"name\": \"Test\", \"country\": %1$d, \"region\": %2$d }",
                country.getId(), syberia.getId());
        RequestBuilder request = put("/cities/{id}", city.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(city.getId()))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.region.id").value(syberia.getId()))
                .andExpect(jsonPath("$.region.name").value("Syberia"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
    }

    @Test
    void update_country_of_city() throws Exception {
        Country russia = entities.country("Russia");
        Country france = entities.country("France");
        Region region = entities.region("Ural", russia);

        City city = entities.city("Test", region, russia);

        String json = String.format("{ \"name\": \"Test\", \"country\": %1$d, \"region\": %2$d }",
                france.getId(), region.getId());
        RequestBuilder request = put("/cities/{id}", city.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(city.getId()))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.region.id").value(region.getId()))
                .andExpect(jsonPath("$.region.name").value("Ural"))
                .andExpect(jsonPath("$.country.id").value(france.getId()))
                .andExpect(jsonPath("$.country.name").value("France"));
    }

    @Test
    void delete_city() throws Exception {
        Country country = entities.country("Russia");
        Region ural = entities.region("Ural", country);

        City city = entities.city("Test", ural, country);

        mvc.perform(delete("/cities/{id}", city.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}