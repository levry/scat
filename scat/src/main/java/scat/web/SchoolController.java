package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.School;
import scat.repo.SchoolRepository;
import scat.web.model.SchoolModel;
import scat.web.search.SchoolSearch;

import javax.validation.Valid;

import static scat.web.search.SchoolSearch.SchoolCriteria;

/**
 * @author levry
 */
@RestController
@RequestMapping("/schools")
public class SchoolController {

    private final SchoolRepository repository;
    private final SchoolSearch schoolSearch;

    public SchoolController(SchoolRepository repository, SchoolSearch schoolSearch) {
        this.repository = repository;
        this.schoolSearch = schoolSearch;
    }

    @GetMapping
    public Iterable<School> index(@ModelAttribute SchoolCriteria criteria) {
        return schoolSearch.findBy(criteria);
    }

    @GetMapping("/{id}")
    public School get(@PathVariable Long id) {
        return repository.findOne(id);
    }

    @PostMapping
    public School save(@Valid @RequestBody SchoolModel data) {
        School school = new School();
        school.setName(data.getName());
        school.setNumber(data.getNumber());
        school.setType(data.getType());
        school.setCity(data.getCity());
        return repository.save(school);
    }

    @PutMapping("/{id}")
    public School update(@PathVariable Long id, @Valid @RequestBody SchoolModel data) {
        School school = repository.getOne(id);
        school.setName(data.getName());
        school.setNumber(data.getNumber());
        school.setType(data.getType());
        school.setCity(data.getCity());
        return repository.save(school);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
