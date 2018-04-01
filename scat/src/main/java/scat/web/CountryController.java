package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.Country;
import scat.repo.CountryRepository;
import scat.web.search.CountrySearch;

import javax.validation.Valid;

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

    @GetMapping
    public Iterable<Country> index(@ModelAttribute Country params) {
        return new CountrySearch(repository).findBy(params);
    }

    @GetMapping("/{id}")
    public Country get(@PathVariable Integer id) {
        return repository.findOne(id);
    }

    @PostMapping
    public Country save(@Valid @RequestBody Country input) {
        return repository.save(input);
    }

    @PutMapping("/{id}")
    public Country update(@PathVariable Integer id, @Valid @RequestBody Country model) {
        Country country = repository.getOne(id);
        country.setName(model.getName());
        return repository.save(model);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
