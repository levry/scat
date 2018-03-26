package scat.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import scat.data.Country;
import scat.repo.CountryRepository;

import javax.persistence.EntityNotFoundException;

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
public class CountryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CountryRepository countryRepository;

    @Test
    public void get_country_by_id() throws Exception {

        Country country = country(66, "Russia");
        when(countryRepository.findOne(66)).thenReturn(country);

        mvc.perform(get("/countries/66")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(66))
                .andExpect(jsonPath("$.name").value("Russia"));
    }

    @Test
    public void should_be_404_if_not_found_by_id() throws Exception {
        when(countryRepository.findOne(4)).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/countries/4")).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void post_country() throws Exception {
        Country country = country(1, "Russia");
        when(countryRepository.save(any(Country.class))).thenReturn(country);

        String json = "{ \"name\": \"Russia\" }";

        RequestBuilder dataPost = post("/countries")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Russia"));
    }

    @Test
    public void update_country() throws Exception {
        Country country = country(5, "France");
        when(countryRepository.getOne(eq(5))).thenReturn(country);
        when(countryRepository.save(any(Country.class))).thenReturn(country);

        String json = "{ \"name\": \"France (update)\" }";

        RequestBuilder dataPost = put("/countries/{id}", country.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("France (update)"));
    }

    @Test
    public void delete_country() throws Exception {
        mvc.perform(delete("/countries/{id}", 66)).andDo(print())
                .andExpect(status().isNoContent());

        verify(countryRepository).deleteById(eq(66));
    }

    private Country country(int id, String name) {
        Country country = new Country();
        country.setId(id);
        country.setName(name);
        return country;
    }
}