package scat.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.Country;
import scat.repo.CountryRepository;
import scat.web.model.CountryModel;

import javax.validation.Valid;

/**
 * @author levry
 */
@RestController
@AllArgsConstructor
@RequestMapping("countries")
public class CountryController {

    private final CountryRepository repository;

    @GetMapping
    public Iterable<Country> index(@ModelAttribute CountryModel criteria) {
        Country example = criteria.createCountry();
        return repository.findBy(example);
    }

    @GetMapping("/{id}")
    public Country get(@PathVariable Integer id) {
        return repository.findOne(id);
    }

    @PostMapping
    public Country save(@Valid @RequestBody CountryModel data) {
        Country country = data.createCountry();
        return repository.save(country);
    }

    @PutMapping("/{id}")
    public Country update(@PathVariable Integer id, @Valid @RequestBody CountryModel data) {
        Country country = repository.getOne(id);
        country.setName(data.getName());
        return repository.save(country);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
