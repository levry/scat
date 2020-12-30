package scat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.domain.model.SchoolType;
import scat.domain.service.SchoolTypeService;
import scat.domain.service.dto.SchoolTypeInput;

import javax.validation.Valid;

/**
 * @author levry
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/school_types")
public class SchoolTypeController {

    private final SchoolTypeService service;

    @GetMapping
    public Iterable<SchoolType> index(@ModelAttribute SchoolTypeInput criteria) {
        return service.findBy(criteria);
    }

    @GetMapping("/{id}")
    public SchoolType get(@PathVariable Integer id) {
        return service.findOne(id);
    }

    @PostMapping
    public SchoolType save(@Valid @RequestBody SchoolTypeInput data) {
        return service.save(data);
    }

    @PutMapping("/{id}")
    public SchoolType update(@PathVariable Integer id, @Valid @RequestBody SchoolTypeInput data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.remove(id);
    }

}
