package scat.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.School;
import scat.domain.service.SchoolService;
import scat.domain.service.dto.SchoolInput;

import javax.validation.Valid;

import static scat.web.search.SchoolSearch.SchoolCriteria;

/**
 * @author levry
 */
@RestController
@AllArgsConstructor
@RequestMapping("/schools")
public class SchoolController {

    private final SchoolService service;

    @GetMapping
    public Iterable<School> index(@ModelAttribute SchoolCriteria criteria) {
        return service.findBy(criteria);
    }

    @GetMapping("/{id}")
    public School get(@PathVariable Long id) {
        return service.findOne(id);
    }

    @PostMapping
    public School save(@Valid @RequestBody SchoolInput data) {
        return service.save(data);
    }

    @PutMapping("/{id}")
    public School update(@PathVariable Long id, @Valid @RequestBody SchoolInput data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
