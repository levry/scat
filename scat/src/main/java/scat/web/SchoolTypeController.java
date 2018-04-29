package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.SchoolType;
import scat.repo.SchoolTypeRepository;
import scat.web.search.SchoolTypeSearch;

import javax.validation.Valid;

/**
 * @author levry
 */
@RestController
@RequestMapping("/school_types")
public class SchoolTypeController {

    private final SchoolTypeRepository repository;

    public SchoolTypeController(SchoolTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<SchoolType> index(@ModelAttribute SchoolType params) {
        return new SchoolTypeSearch(repository).findBy(params);
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
        repository.deleteById(id);
    }

}
