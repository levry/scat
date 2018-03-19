package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.City;
import scat.repo.CityRepository;
import scat.web.model.CityModel;

import javax.validation.Valid;

/**
 * @author levry
 */
@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityRepository repository;

    public CityController(CityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<City> index() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public City get(@PathVariable Long id) {
        return repository.findOne(id);
    }

    @PostMapping
    public City save(@Valid @RequestBody CityModel model) {
        City city = new City();
        city.setName(model.getName());
        city.setRegion(model.getRegion());
        city.setCountry(model.getCountry());
        return repository.save(city);
    }

    @PutMapping("/{id}")
    public City update(@PathVariable Long id, @Valid @RequestBody City model) {
        City city = repository.getOne(id);
        city.setName(model.getName());
        city.setRegion(model.getRegion());
        city.setCountry(model.getCountry());
        return repository.save(city);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.delete(id);
    }

}
