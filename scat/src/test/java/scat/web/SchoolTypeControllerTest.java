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
import scat.data.SchoolType;
import scat.repo.SchoolTypeRepository;

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
public class SchoolTypeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SchoolTypeRepository repository;

    @Test
    public void get_school_type() throws Exception {
        SchoolType schoolType = new SchoolType();
        schoolType.setId(2);
        schoolType.setName("Academy");

        when(repository.findOne(eq(2))).thenReturn(schoolType);

        mvc.perform(get("/school_types/{id}", 2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Academy"));
    }

    @Test
    public void should_be_404_if_not_found_type() throws Exception {
        when(repository.findOne(eq(6))).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/school_types/{id}", 6)).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void post_school_type() throws Exception {

        when(repository.save(any(SchoolType.class))).then(invocation -> {
            SchoolType input = invocation.getArgument(0);
            input.setId(1);
            return input;
        });

        String json = "{ \"name\": \"University\" }";

        RequestBuilder dataPost = post("/school_types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("University"));
    }

    @Test
    public void update_school_type() throws Exception {

        SchoolType schoolType = new SchoolType();
        schoolType.setId(5);
        schoolType.setName("Colledge");

        when(repository.getOne(eq(schoolType.getId()))).thenReturn(schoolType);
        when(repository.save(any(SchoolType.class))).thenReturn(schoolType);

        String json = "{ \"name\": \"University\" }";

        RequestBuilder dataPost = put("/school_types/5")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json);

        mvc.perform(dataPost).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("University"));
    }

    @Test
    public void delete_school_type() throws Exception {
        mvc.perform(delete("/school_types/{id}", 6)).andDo(print())
                .andExpect(status().isNoContent());

        verify(repository).deleteById(eq(6));
    }
}