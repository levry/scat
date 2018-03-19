package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.School;
import scat.repo.SchoolRepository;
import scat.web.model.SchoolModel;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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
    public Iterable<School> index() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public School get(@PathVariable Long id) {
        return repository.getOne(id);
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
        repository.delete(id);
    }
}
