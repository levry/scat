package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.School;
import scat.repo.SchoolRepository;
import scat.web.model.SchoolModel;
import scat.web.search.SchoolSearch;

import javax.validation.Valid;

import static scat.web.search.SchoolSearch.*;

/**
 * @author levry
 */
@RestController
@RequestMapping("/schools")
public class SchoolController {

    private final SchoolRepository repository;

    public SchoolController(SchoolRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<School> index(@ModelAttribute SchoolCriteria params) {
        return new SchoolSearch(repository).findBy(params);
    }

    @GetMapping("/{id}")
    public School get(@PathVariable Long id) {
        return repository.findOne(id);
    }

    @PostMapping
    public School save(@Valid @RequestBody SchoolModel model) {
        School school = new School();
        school.setName(model.getName());
        school.setNumber(model.getNumber());
        school.setType(model.getType());
        school.setCity(model.getCity());
        return repository.save(school);
    }

    @PutMapping("/{id}")
    public School update(@PathVariable Long id, @Valid @RequestBody SchoolModel model) {
        School school = repository.getOne(id);
        school.setName(model.getName());
        school.setNumber(model.getNumber());
        school.setType(model.getType());
        school.setCity(model.getCity());
        return repository.save(school);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
