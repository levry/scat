package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.Country;
import scat.repo.CountryRepository;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author levry
 */
@RestController
@RequestMapping("countries")
public class CountryController {

    private final CountryRepository repository;

    public CountryController(CountryRepository repository) {
        this.repository = repository;
    }

    @RequestMapping
    public Iterable<Country> index() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{id}", method = GET)
    public Country get(@PathVariable Integer id) {
        return repository.findOne(id);
    }

    @RequestMapping(method = POST)
    public Country save(@Valid @RequestBody Country input) {
        return repository.save(input);
    }

    @RequestMapping(value = "/{id}", method = PUT)
    public Country update(@PathVariable Integer id, @Valid @RequestBody Country model) {
        Country country = repository.getOne(id);
        country.setName(model.getName());
        return repository.save(model);
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository.delete(id);
    }
}
