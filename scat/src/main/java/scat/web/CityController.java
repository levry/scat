package scat.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.City;
import scat.repo.CityRepository;
import scat.web.model.CityModel;

import javax.validation.Valid;

import static scat.web.search.CitySearch.*;

/**
 * @author levry
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityRepository repository;

    @GetMapping
    public Iterable<City> index(@ModelAttribute CityCriteria criteria) {
        return repository.findBy(criteria);
    }

    @GetMapping("/{id}")
    public City get(@PathVariable Long id) {
        return repository.findOne(id);
    }

    @PostMapping
    public City save(@Valid @RequestBody CityModel data) {
        City city = new City();
        city.setName(data.getName());
        city.setRegion(data.getRegion());
        city.setCountry(data.getCountry());
        return repository.save(city);
    }

    @PutMapping("/{id}")
    public City update(@PathVariable Long id, @Valid @RequestBody CityModel data) {
        City city = repository.getOne(id);
        city.setName(data.getName());
        city.setRegion(data.getRegion());
        city.setCountry(data.getCountry());
        return repository.save(city);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
