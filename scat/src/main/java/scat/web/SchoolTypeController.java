package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.SchoolType;
import scat.repo.SchoolTypeRepository;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author levry
 */
// TODO выделить тестовую конфигурацию (отдельная бд и тд)
@RestController
@RequestMapping("/school_types")
public class SchoolTypeController {

    private final SchoolTypeRepository repository;

    public SchoolTypeController(SchoolTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<SchoolType> index() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public SchoolType get(@PathVariable Integer id) {
        return repository.findOne(id);
    }

    @PostMapping
    public SchoolType save(@Valid @RequestBody SchoolType model) {
        return repository.save(model);
    }

    @PutMapping("/{id}")
    public SchoolType update(@PathVariable Integer id, @Valid @RequestBody SchoolType model) {
        SchoolType schoolType = repository.getOne(id);
        schoolType.setName(model.getName());
        return repository.save(schoolType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository.delete(id);
    }

}
