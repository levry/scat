package scat.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import scat.Entities;
import scat.data.City;
import scat.data.Country;
import scat.data.Region;
import scat.repo.CityRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CityControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityRepository cityRepository;

    @Autowired
    private EntityManager entityManager;

    private Entities entities;

    @BeforeEach
    void setUp() {
        entities = new Entities(entityManager);
    }


    @Test
    @Transactional
    void get_city() throws Exception {

        Country country = entities.country("Russia");
        Region region = entities.region("Ural", country);
        City city = entities.city("Ekaterinburg", region, country);
        when(cityRepository.findOne(eq(city.getId()))).thenReturn(city);

        mvc.perform(get("/cities/{id}", city.getId())).andDo(print())
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
        when(cityRepository.findOne(eq(3L))).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/cities/{id}", 3)).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void post_city() throws Exception {
        Country country = entities.country("Russia");
        Region region = entities.region("Ural", country);

        when(cityRepository.save(any(City.class))).then(invocation -> {
            City city = invocation.getArgument(0);
            city.setId(2L);
            return city;
        });


        String json = format("{ \"name\": \"Test\", \"country\": %1$d, \"region\": %2$d}",
                country.getId(), region.getId());
        RequestBuilder dataPost = post("/cities")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"))
                .andExpect(jsonPath("$.region.id").value(region.getId()))
                .andExpect(jsonPath("$.region.name").value("Ural"));
    }

    @Test
    @Transactional
    void save_city_without_region() throws Exception {
        Country country = entities.country("Russia");

        when(cityRepository.save(any(City.class))).then(invocation -> {
            City city = invocation.getArgument(0);
            city.setId(2L);
            return city;
        });


        String json = format("{ \"name\": \"Test\", \"country\": %1$d }", country.getId());
        RequestBuilder dataPost = post("/cities")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
                //.andExpect(jsonPath("$.region.id").value(null))
                //.andExpect(jsonPath("$.region.name").value(null));
    }

    @Test
    @Transactional
    void update_name_of_city() throws Exception {
        Country country = entities.country("Russia");
        Region region = entities.region("Ural", country);

        City city = entities.city("Test", region, country);

        when(cityRepository.getOne(eq(city.getId()))).thenReturn(city);
        when(cityRepository.save(any(City.class))).then(invocation -> invocation.getArgument(0));

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
    @Transactional
    void update_region_of_city() throws Exception {
        Country country = entities.country("Russia");
        Region ural = entities.region("Ural", country);
        Region syberia = entities.region("Syberia", country);

        City city = entities.city("Test", ural, country);

        when(cityRepository.getOne(eq(city.getId()))).thenReturn(city);
        when(cityRepository.save(any(City.class))).then(invocation -> invocation.getArgument(0));

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
    @Transactional
    void update_country_of_city() throws Exception {
        Country russia = entities.country("Russia");
        Country france = entities.country("France");
        Region region = entities.region("Ural", russia);

        City city = entities.city("Test", region, russia);

        when(cityRepository.getOne(eq(city.getId()))).thenReturn(city);
        when(cityRepository.save(any(City.class))).then(invocation -> invocation.getArgument(0));

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
        mvc.perform(delete("/cities/{id}", 15)).andDo(print())
                .andExpect(status().isNoContent());

        verify(cityRepository).deleteById(eq(15L));
    }
}