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
import scat.Entities;
import scat.data.Country;
import scat.data.Region;
import scat.repo.CountryRepository;
import scat.repo.RegionRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
public class RegionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private CountryRepository countryRepository;

    @Autowired
    private EntityManager entityManager;

    private Entities entities;

    @Before
    public void setUp() {
        entities = new Entities(entityManager);
    }

    @Test
    @Transactional
    public void getRegion() throws Exception {

        Country country = entities.country("Russia");
        Region region = entities.region("Sverdlovskaya obl", country);
        when(regionRepository.findOne(eq(region.getId()))).thenReturn(region);

        mvc.perform(get("/regions/{id}", region.getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(region.getId()))
                .andExpect(jsonPath("$.name").value("Sverdlovskaya obl"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
    }

    @Test
    @Transactional
    public void postRegion() throws Exception {

        Country country = entities.country("Russia");

        when(regionRepository.save(any(Region.class))).then(invocation -> {
            Region region = invocation.getArgumentAt(0, Region.class);
            region.setId(2);
            return region;
        });


        String json = format("{ \"name\": \"Test\", \"country\": %1$s}", country.getId());
        RequestBuilder dataPost = post("/regions")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.country.id").value(country.getId()))
                .andExpect(jsonPath("$.country.name").value("Russia"));
    }

    @Test
    public void shouldBeBadRequestIfNotFoundCountry() throws Exception {
        when(countryRepository.findOne(66)).thenReturn(null);

        RequestBuilder dataPost = post("/regions")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{ \"name\": \"Test\", \"country\": 66}");

        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateNameOfRegion() throws Exception {
        Country country = entities.country("Russia");
        Region region = entities.region("Test", country);

        when(regionRepository.save(any(Region.class))).thenReturn(region);
        when(regionRepository.getOne(eq(region.getId()))).thenReturn(region);


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
    @Transactional
    public void updateCountryOfRegion() throws Exception {
        Country russia = entities.country("Russia");
        Country france = entities.country("France");

        Region region = entities.region("Test", russia);

        when(regionRepository.save(any(Region.class))).thenReturn(region);
        when(regionRepository.getOne(eq(region.getId()))).thenReturn(region);


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
    public void deleteRegion() throws Exception {

        mvc.perform(delete("/regions/{id}", 15)).andDo(print())
                .andExpect(status().isNoContent());

        verify(regionRepository).delete(eq(15));
    }

}