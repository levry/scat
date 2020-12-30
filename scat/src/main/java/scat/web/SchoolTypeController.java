package scat.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.SchoolType;
import scat.repo.SchoolTypeRepository;
import scat.web.model.SchoolTypeModel;

import javax.validation.Valid;

/**
 * @author levry
 */
@RestController
@AllArgsConstructor
@RequestMapping("/school_types")
public class SchoolTypeController {

    private final SchoolTypeRepository repository;

    @GetMapping
    public Iterable<SchoolType> index(@ModelAttribute SchoolTypeModel criteria) {
        SchoolType example = criteria.createSchoolType();
        return repository.findBy(example);
    }

    @GetMapping("/{id}")
    public SchoolType get(@PathVariable Integer id) {
        return repository.findOne(id);
    }

    @PostMapping
    public SchoolType save(@Valid @RequestBody SchoolTypeModel data) {
        SchoolType type = data.createSchoolType();
        return repository.save(type);
    }

    @PutMapping("/{id}")
    public SchoolType update(@PathVariable Integer id, @Valid @RequestBody SchoolTypeModel data) {
        SchoolType schoolType = repository.getOne(id);
        schoolType.setName(data.getName());
        return repository.save(schoolType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }

}
