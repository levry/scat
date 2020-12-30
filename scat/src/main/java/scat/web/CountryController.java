package scat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.domain.model.Country;
import scat.domain.service.CountryService;
import scat.domain.service.dto.CountryInput;

import javax.validation.Valid;

/**
 * @author levry
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("countries")
public class CountryController {

    private final CountryService service;

    @GetMapping
    public Iterable<Country> index(@ModelAttribute CountryInput criteria) {
        return service.findBy(criteria);
    }

    @GetMapping("/{id}")
    public Country get(@PathVariable Integer id) {
        return service.findOne(id);
    }

    @PostMapping
    public Country save(@Valid @RequestBody CountryInput data) {
        return service.save(data);
    }

    @PutMapping("/{id}")
    public Country update(@PathVariable Integer id, @Valid @RequestBody CountryInput data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.remove(id);
    }
}
